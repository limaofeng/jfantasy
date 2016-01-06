package org.jfantasy.album.bean;

import org.jfantasy.framework.dao.BaseBusEntity;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "PHOTO_ALBUM")
public class Photo extends BaseBusEntity {

    @Id
    @Column(name = "ID", insertable = true, updatable = false)
    @GeneratedValue(generator = "fantasy-sequence")
    @GenericGenerator(name = "fantasy-sequence", strategy = "fantasy-sequence")
    private Long id;
    /**
     * 名称
     */
    @Column(name = "NAME", length = 50, nullable = false)
    private String name;
    /**
     * 描述
     */
    @Column(name = "DESCRIPTION", length = 350)
    private String description;
    /**
     * 拍摄时间
     */
    @Column(name = "TIME")
    private Date time;
    /**
     * 拍摄地点
     */
    @Column(name = "POI_NAME", length = 100)
    private String poiName;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="PHOTO_ALBUM_ID",foreignKey = @ForeignKey(name = "FK_PHOTO_ALBUM_PHOTO"))
    private PhotoAlbum photoAlbum;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getTime() {
        if (this.time == null) {
            return null;
        }
        return (Date) this.time.clone();
    }

    public void setTime(Date time) {
        if (time == null) {
            this.time = null;
        } else {
            this.time = (Date) time.clone();
        }
    }

    public String getPoiName() {
        return poiName;
    }

    public void setPoiName(String poiName) {
        this.poiName = poiName;
    }

    public PhotoAlbum getPhotoAlbum() {
        return photoAlbum;
    }

    public void setPhotoAlbum(PhotoAlbum photoAlbum) {
        this.photoAlbum = photoAlbum;
    }
}
