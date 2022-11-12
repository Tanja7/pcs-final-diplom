import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class SearchResult {
    private final List<PageEntry> pages;

    public SearchResult() {
        pages = new ArrayList<>();
    }

    public SearchResult(List<PageEntry> pages) {
        this.pages = pages;
    }

    public void addPages(List<PageEntry> newPages) {
        for (var p : newPages) {
            addPage(p);
        }
    }

    void addPage(PageEntry page) {
        //Запускаем цикл по страницам pages и ищем среди них страницу page
        for (var p : pages) {
            //Сравниваем имя пдф файла и номера страницы p с именем пдф файла и номером страницы искомой страницы page
            if (page.getPdfName().equals(p.getPdfName()) && page.getPage() == p.getPage()) {
                //Если добавляемая страница есть в списке, то прибавляем количество слов на этой странице для p
                p.setCount(p.getCount() + page.getCount());
                return;
            }
        }
        // Если в списке pages нет добавляемой страницы, то добавим страницу в список pages
        pages.add(page);
    }

    public List<PageEntry> getPages() {
        return pages;
    }

    public String toJson() {
        if (pages.isEmpty())
            return "Ничего не найдено";
        Gson gson = new GsonBuilder()
                .setPrettyPrinting()
                .create();
        return gson.toJson(pages);
    }

    // Сортирует список по количеству слов от большего к меньшему
    public void sort() {
        Collections.sort(pages);
    }
}
