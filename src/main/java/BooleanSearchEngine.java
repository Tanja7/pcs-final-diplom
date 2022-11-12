import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.canvas.parser.PdfTextExtractor;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class BooleanSearchEngine implements SearchEngine {
    // Индексированные слова: ключ - слово, значение объект (искомый список)
    HashMap<String, List<PageEntry>> wordsIndexing = new HashMap<>(); //indexedWords
    // Список стоп слов, которые не нужно учитывать при поиске
    //  private final Set<String> stopWords = new HashSet<>();
    public StopWords stopWords = new StopWords(new File("stop-ru.txt"));

    public BooleanSearchEngine(File pdfsDir) throws IOException {
        // прочтите тут все pdf и сохраните нужные данные,
        // тк во время поиска сервер не должен уже читать файлы
        File[] pdfFiles = pdfsDir.listFiles();///listOfFiles
        if (pdfFiles != null) {
            // ищем в каждом файле
            for (File file : pdfFiles) {
                if (file.isFile()) {
                    var doc = new PdfDocument(new PdfReader(file));
                    int countPages = doc.getNumberOfPages(); //pagesCount
                    for (int i = 1; i <= countPages; i++) {
                        var page = doc.getPage(i);
                        var text = PdfTextExtractor.getTextFromPage(page);
                        var words = text.split("\\P{IsAlphabetic}+");
                        // подсчёт частоты слов
                        Map<String, Integer> freqs = new HashMap<>();
                        // мапа, где ключ - слово, а значение - частота
                        for (var word : words) { // перебираем слова
                            if (word.isEmpty()) {
                                continue;
                            }
                            word = word.toLowerCase();
                            freqs.put(word, freqs.getOrDefault(word, 0) + 1);
                        }
                        // добавление в индекс
                        for (var entry : freqs.entrySet()) {
                            addWord(entry.getKey(), file.getName(), i, entry.getValue());
                        }
                    }
                }
            }
        }
        // отсортируем списки
        for (var entry : wordsIndexing.entrySet()) {
            Collections.sort(entry.getValue());
        }
    }

        @Override
        public List<PageEntry> search (String text){
            // Разбить текст на слова.
            // Пройти по всем словам и попробовать найти.
            // Если слово в стоп листе, то пропустить.
            // В результате будет общий список страниц, а количество найденных на каждой
            // странице будет суммироваться для каждого слова.

            var words = text.split("\\P{IsAlphabetic}+");

            SearchResult searchResult = new SearchResult();
            for (var word : words) {
                var word1 = word.toLowerCase();
                if (stopWords.contains(word1))
                    continue;
                if (wordsIndexing.containsKey(word1)) {
                    searchResult.addPages(wordsIndexing.get(word1));
                }
            }
            searchResult.sort();//Сортируем результат
            return searchResult.getPages();

        }


        private void addWord (String word, String pdfName,int pageNumber, int count){ //addWordToIndex
            if (wordsIndexing.containsKey(word)) {
                var pages = wordsIndexing.get(word);
                pages.add(new PageEntry(pdfName, pageNumber, count));
            } else {
                wordsIndexing.put(word, new ArrayList<>());
                wordsIndexing.get(word).add(new PageEntry(pdfName, pageNumber, count));
            }
        }


//    public List<PageEntry> totalNumber(List<PageEntry> resultWord, List<PageEntry> result) {
//        // Пройти по всем найденным страницам
//        for (var pe : resultWord) {
//
//            // Для каждой найденной страницы Пройти по страницам в результирующем списке.
//            // Если там есть такая страница, то добавить количество совпадений искомого слова.
//            int addedCount = pe.getCount();
//            // Запомним в эту переменную страницу из результирующего списка, если она там есть.
//            PageEntry addedPageEntry = null;
//            // Ищем страницу в результирующем списке
//            for (var pe1 : result) {
//                // Ищем по названию файла и номеру странице
//                if (pe.getPdfName().compareTo(pe1.getPdfName()) == 0 && pe.getPage() == pe1.getPage()) {
//                    // Если нашли, то прибавлем количество совпадений.
//                    pe1.setCount(pe1.getCount() + addedCount);
//                    addedPageEntry = pe1;// Запоминаем страницу
//                    break;// выходим из цикла
//                }
//            }
//            // Если страница не найдена (еще не добавлена в результат), то добавим
//            if (addedPageEntry == null)
//                result.add(pe);
//        }
//        return result;
//    }
    }




