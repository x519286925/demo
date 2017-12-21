package com.test.demo.entity.monEntity;

import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.persistence.Id;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by yhn on 2017/11/8.
 */
@Document
@Data
public class File {
    @Id
    private String id;
    private String name; 
    private String contentType;  //文件类型
    private long size;
    private String uploadDate;
    private String md5;
    private byte[] content; //文件内容
    protected  File(){}
    public File(String name, String contentType, long size,byte[] content) {
        this.name = name;
        this.contentType = contentType;
        this.size = size;
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        this.uploadDate = df.format(new Date());
        this.content = content;
    }
    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (object == null || getClass() != object.getClass()) {
            return false;
        }
        File fileInfo = (File) object;
        return java.util.Objects.equals(size, fileInfo.size)
                && java.util.Objects.equals(name, fileInfo.name)
                && java.util.Objects.equals(contentType, fileInfo.contentType)
                && java.util.Objects.equals(uploadDate, fileInfo.uploadDate)
                && java.util.Objects.equals(md5, fileInfo.md5)
                && java.util.Objects.equals(id, fileInfo.id);
    }

    @Override
    public int hashCode() {
        return java.util.Objects.hash(name, contentType, size, uploadDate, md5, id);
    }

    @Override
    public String toString() {
        return "File{"
                + "name='" + name + '\''
                + ", contentType='" + contentType + '\''
                + ", size=" + size
                + ", uploadDate=" + uploadDate
                + ", md5='" + md5 + '\''
                + ", id='" + id + '\''
                + '}';
    }
}
