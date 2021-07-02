package org.techtown.amatta;

public class Todo {
    long id;
    String title;
    int start_h;
    int start_m;
    int end_h;
    int end_m;
    String memo;
    String emoji;
    boolean tf;
    String year;
    String mon;
    String day;

    public Todo(long id, String title, int start_h, int start_m, int end_h, int end_m, String memo,
                String emoji, boolean tf, String year, String mon, String day) {
        this.id = id;
        this.title = title;
        this.start_h = start_h;
        this.start_m = start_m;
        this.end_h = end_h;
        this.end_m = end_m;
        this.memo = memo;
        this.emoji = emoji;
        this.tf = tf;
        this.year = year;
        this.mon = mon;
        this.day = day;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getStart_h() {
        return start_h;
    }

    public void setStart_h(int start_h) {
        this.start_h = start_h;
    }

    public int getStart_m() {
        return start_m;
    }

    public void setStart_m(int start_m) {
        this.start_m = start_m;
    }

    public int getEnd_h() {
        return end_h;
    }

    public void setEnd_h(int end_h) {
        this.end_h = end_h;
    }

    public int getEnd_m() {
        return end_m;
    }

    public void setEnd_m(int end_m) {
        this.end_m = end_m;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public boolean getTf() { return tf; }

    public void setTf(boolean tf) { this.tf = tf; }

    public String getYear() { return year; }

    public void setYear(String year) { this.year = year; }

    public String getMon() { return mon; }

    public void setMon(String mon) { this.mon = mon; }

    public String getDay() { return day; }

    public void setDay(String day) { this.day = day; }

    public String getEmoji() { return emoji; }

    public void setEmoji(String emoji) { this.emoji = emoji; }
}
