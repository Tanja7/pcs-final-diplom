import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.canvas.parser.PdfTextExtractor;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class BooleanSearchEngine implements SearchEngine {

    HashMap<String, List<PageEntry>> wordIndexing = new HashMap<>(); // ключ - слово, значение объект (искомый список)
    // StopWords stopWords = new StopWords();
    public BooleanSearchEngine(File pdfsDir) throws IOException {
        // прочтите тут все pdf и сохраните нужные данные,
        // тк во время поиска сервер не должен уже читать файлы
        File[] pdfFiles = pdfsDir.listFiles();
        if (pdfFiles != null) {
            // ищем в каждом файле
            for (File pdf : pdfFiles) {
                if (pdf.isFile()) {
                    var doc = new PdfDocument(new PdfReader(pdf));
                    int countPages = doc.getNumberOfPages();
                    for (int i = 1; i <= countPages; i++) {
                        var page = doc.getPage(i);
                        var text = PdfTextExtractor.getTextFromPage(page);
                        var words = text.split("\\P{IsAlphabetic}+");
                        Map<String, Integer> freqs = new HashMap<>(); // мапа, где ключом будет слово, а значением - частота
                        for (var word : words) { // перебираем слова
                            if (word.isEmpty()) {
                                continue;
                            }
                            word = word.toLowerCase();
                            freqs.put(word, freqs.getOrDefault(word, 0) + 1);
                        }
                        for (var entry : freqs.entrySet()) {
                            addWord(entry.getKey(), pdf.getName(), i, entry.getValue());
                        }
                    }
                }
            }
        }
        for (var entry : wordIndexing.entrySet()) {
            Collections.sort(entry.getValue());
        }
    }

    @Override
    public List<PageEntry> search(String word) {
        // тут реализуйте поиск по слову
        String word1 = word.toLowerCase();
        if (wordIndexing.containsKey(word1)) {
            return wordIndexing.get(word1);
        } else {
            return Collections.emptyList();
        }
    }

    private void addWord(String word, String pdfName, int pageNumber, int count) {
        if (wordIndexing.containsKey(word)) {
            var pages = wordIndexing.get(word);
            pages.add(new PageEntry(pdfName, pageNumber, count));
        } else {
            wordIndexing.put(word, new ArrayList<>());
            wordIndexing.get(word).add(new PageEntry(pdfName, pageNumber, count));
        }
    }
    public List<PageEntry> totalNumber(List<PageEntry> resultWord, List<PageEntry>result) {
        // Пройти по всем найденным страницам
        for (var pe : resultWord) {

            // Для каждой найденной страницы Пройти по страницам в результирующем списке.
            // Если там есть такая страница, то добавить количество совпадений искомого слова.
            int addedCount = pe.getCount();
            // Запомним в эту переменную страницу из результирующего списка, если она там есть.
            PageEntry addedPageEntry = null;
            // Ищем страницу в результирующем списке
            for (var pe1 : result) {
                // Ищем по названию файла и номеру странице
                if(pe.getPdfName().compareTo(pe1.getPdfName()) == 0 && pe.getPage() == pe1.getPage()) {
                    // Если нашли, то прибавлем количество совпадений.
                    pe1.setCount(pe1.getCount() + addedCount);
                    addedPageEntry = pe1;// Запоминаем страницу
                    break;// выходим из цикла
                }
            }
            // Если страница не найдена (еще не добавлена в результат), то добавим
            if(addedPageEntry == null)
                result.add(pe);
        }
        return result;
    }
}