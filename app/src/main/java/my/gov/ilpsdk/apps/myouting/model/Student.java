package my.gov.ilpsdk.apps.myouting.model;


import java.io.Serializable;

public class Student implements Serializable {
    String ndp;
    String noic;
    String fullname;
    String mobile_number;
    String parent_name;
    String parent_mobile_number;
    String course_name;
    String course_code;

    public Student(String ndp, String noic, String fullname, String mobile_number, String parent_name, String parent_mobile_number, String course_name, String course_code) {
        this.ndp = ndp;
        this.noic = noic;
        this.fullname = fullname;
        this.mobile_number = mobile_number;
        this.parent_name = parent_name;
        this.parent_mobile_number = parent_mobile_number;
        this.course_name = course_name;
        this.course_code = course_code;
    }

    public String getNdp() {
        return ndp;
    }

    public void setNdp(String ndp) {
        this.ndp = ndp;
    }

    public String getNoic() {
        return noic;
    }

    public void setNoic(String noic) {
        this.noic = noic;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getMobile_number() {
        return mobile_number;
    }

    public void setMobile_number(String mobile_number) {
        this.mobile_number = mobile_number;
    }

    public String getParent_name() {
        return parent_name;
    }

    public void setParent_name(String parent_name) {
        this.parent_name = parent_name;
    }

    public String getParent_mobile_number() {
        return parent_mobile_number;
    }

    public void setParent_mobile_number(String parent_mobile_number) {
        this.parent_mobile_number = parent_mobile_number;
    }

    public String getCourse_name() {
        return course_name;
    }

    public void setCourse_name(String course_name) {
        this.course_name = course_name;
    }

    public String getCourse_code() {
        return course_code;
    }

    public void setCourse_code(String course_code) {
        this.course_code = course_code;
    }
}
