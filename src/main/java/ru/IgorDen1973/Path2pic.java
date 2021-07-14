package ru.IgorDen1973;

public enum Path2pic {

    REGULAR("src/test/resources/pic_02.jpg"),
    RESPONCE_CHECK("src/test/resources/Response_Check.jpg"),
    PDF("src/test/resources/Pic_PDF.pdf"),
    NOT_IMAGE("src/test/resources/Main.java"),
    BINARY("src/test/resources/899.bin");

    private final String sticker;

    Path2pic(String sticker) {
        this.sticker = sticker;
    }

    public String getSticker() {
        return sticker;
    }
}
