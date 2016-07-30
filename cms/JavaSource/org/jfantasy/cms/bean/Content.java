package org.jfantasy.cms.bean;

import com.fasterxml.jackson.annotation.JsonFilter;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import io.swagger.annotations.ApiModel;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.jfantasy.framework.dao.BaseBusEntity;
import org.jfantasy.framework.jackson.JSON;

import javax.persistence.*;
import java.io.IOException;

/**
 * 内容表
 *
 * @author 李茂峰
 * @version 1.0
 * @since 2012-11-4 下午05:47:36
 */
@ApiModel(value = "内容表", description = "用于分离大的文本对象,例如文章的正文")
@Entity
@Table(name = "CMS_CONTENT")
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@JsonIgnoreProperties({"hibernate_lazy_initializer", "handler"})
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
                jgen.writeString(content.toString());
            }
        }

    }

}
