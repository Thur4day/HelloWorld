/**
 * CopyRight	2014 ZhuYan
 *
 * @author Zhu Yan
 * <p/>
 * All right reserved
 * <p/>
 * Created	on	2014-3-19  ����10:08:36
 */
package com.dxz.helloworld.xutilsdb;

import org.xutils.db.annotation.Column;
import org.xutils.db.annotation.Table;

/**
 * @author Zhu Yan
 *
 * Created	on	2014-3-19  ����10:08:36
 */
@Table(name = "users")
public class User {

    @Column(name = "id", isId = true)
    private int id;

    @Column(name = "name")
    private String name;

    @Column(name = "phone")
    private String phone;


    /**
     * @return the id
     */
    public int getId() {
        return id;
    }


    /**
     * @param id the id to set
     */
    public void setId(int id) {
        this.id = id;
    }


    /**
     * @return the name
     */
    public String getName() {
        return name;
    }


    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }


    /**
     * @return the phone
     */
    public String getPhone() {
        return phone;
    }


    /**
     * @param phone the phone to set
     */
    public void setPhone(String phone) {
        this.phone = phone;
    }


    /**
     *
     */
    public User() {
        super();
    }

}
