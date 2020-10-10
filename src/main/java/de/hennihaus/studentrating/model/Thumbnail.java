package de.hennihaus.studentrating.model;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class Thumbnail {

    private String url;
    private String title;

    public Thumbnail(ThumbnailWrapper thumbnail) {
        url = thumbnail.getUrl();
        title = thumbnail.getTitle();
    }

    @Setter
    @Getter
    @Accessors(chain = true)
    public static class ThumbnailWrapper {

        private String url;
        private String title;

        public Thumbnail wrap() {
            return new Thumbnail(this);
        }
    }
}
