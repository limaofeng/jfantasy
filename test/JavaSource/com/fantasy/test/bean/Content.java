package org.jfantasy.test.bean;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.jfantasy.framework.dao.BaseBusEntity;
import org.jfantasy.system.util.SettingUtil;

import java.io.IOException;

/**
 * 内容表
 *
 * @author 李茂峰
 * @version 1.0
 * @since 2012-11-4 下午05:47:36
 */
@Entity
@Table(name = "CMS_CONTENT")
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Content extends BaseBusEntity {

    private static final long serialVersionUID = -7570871629827875364L;

    @Id
    @Column(name = "ID", nullable = false, insertable = true, updatable = true, precision = 22, scale = 0)
    @GeneratedValue(generator = "fantasy-sequence")
    @org.hibernate.annotations.GenericGenerator(name = "fantasy-sequence", strategy = "fantasy-sequence")
    private Long id;
    /**
     * 正文内容
     */
    @Lob
    @Column(name = "CONTENT")
    private String text;

    public Content() {
    }

    public Content(String text) {
        this.setText(text);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    @Override
    public String toString() {
        return this.text == null ? "" : this.text;
    }

    public static class ContentSerialize extends JsonSerializer<Content> {

        @Override
        public void serialize(Content content, JsonGenerator jgen, SerializerProvider provider) throws IOException {
            if (content == null) {
                jgen.writeString("");
            } else {
                jgen.writeString(SettingUtil.toHtml(content.toString()));
            }
        }

    }

}
