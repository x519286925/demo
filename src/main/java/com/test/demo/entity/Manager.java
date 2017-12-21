package com.test.demo.entity;

import lombok.Data;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.Entity;
import javax.persistence.Id;

/**
 * Created by yhn on 2017/11/13.
 */
@Entity
@Data
@DynamicUpdate
public class Manager {
  @Id
  private String id;   //id
  private String openid; //用户openid
  private String power; //权限
}
