package com.test.demo.utils;

import com.mongodb.DB;
import com.mongodb.gridfs.GridFS;
import com.mongodb.gridfs.GridFSDBFile;
import com.mongodb.gridfs.GridFSInputFile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.io.File;
@Component
public class MongoTemplateServiceImpl {
    @Autowired
    private MongoTemplate mongoTemplate;
    /**
     * 存储文件
     * @param collectionName 集合名
     * @param file 文件
     * @param fileid 文件id
     * @param companyid 文件的公司id
     * @param filename 文件名称
     */
    public void SaveFile(String collectionName, File file, String fileid, String companyid, String filename) {
        try {
            DB db = mongoTemplate.getDb();
            // 存储fs的根节点
            GridFS gridFS = new GridFS(db, collectionName);
            GridFSInputFile gfs = gridFS.createFile(file);
            gfs.put("aliases", companyid);
            gfs.put("filename", fileid);
            gfs.put("contentType", filename.substring(filename.lastIndexOf(".")));
            gfs.save();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("存储文件时发生错误！！！");
        }
    }

    // 取出文件
    public GridFSDBFile retrieveFileOne(String collectionName, String filename) {
        try {
            DB db = mongoTemplate.getDb();
            // 获取fs的根节点
            GridFS gridFS = new GridFS(db, collectionName);
            GridFSDBFile dbfile = gridFS.findOne(filename);
            if (dbfile != null) {
                return dbfile;
            }
        } catch (Exception e) {
            // TODO: handle exception
        }
        return null;
    }
}
