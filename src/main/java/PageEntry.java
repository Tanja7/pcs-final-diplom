import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class PageEntry implements Comparable<PageEntry> {
    private final String pdfName;
    private final int page;

    private int count;

    public PageEntry(String pdfName, int page, int count) {
        this.pdfName = pdfName;
        this.page = page;
        this.count = count;
    }

    @Override
    public int compareTo(PageEntry o) {

        return Integer.compare(o.count, count);
    }

    @Override
    public String toString() {
        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.create();

        var string = gson.toJson(this);
        return string;

    }

    public String getPdfName() {
        return pdfName;
    }

    public int getPage() {
        return page;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}