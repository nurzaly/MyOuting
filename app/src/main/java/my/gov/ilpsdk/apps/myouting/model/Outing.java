package my.gov.ilpsdk.apps.myouting.model;

import java.time.LocalDateTime;

public class Outing {
    private int id;
    private String ndp;
    private String out_time;
    private String return_time;
    private String gate_in_time;
    private String gate_out_time;
    private String description;
    private int status;
    private String approved_by;
    private String approved_date;
    private String remarks;

    public Outing(int id, String ndp, String out_time, String return_time, String gate_in_time, String gate_out_time, String description, int status, String approved_by, String approved_date, String remaks) {
        this.id = id;
        this.ndp = ndp;
        this.out_time = out_time;
        this.return_time = return_time;
        this.gate_in_time = gate_in_time;
        this.gate_out_time = gate_out_time;
        this.description = description;
        this.status = status;
        this.approved_by = approved_by;
        this.approved_date = approved_date;
        this.remarks = remaks;
    }

    public Outing(String ndp, String out_time, String return_time, String description) {
        this.ndp = ndp;
        this.out_time = out_time;
        this.return_time = return_time;
        this.gate_in_time = gate_in_time;
        this.gate_out_time = gate_out_time;
        this.description = description;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNdp() {
        return ndp;
    }

    public void setNdp(String ndp) {
        this.ndp = ndp;
    }

    public String getOut_time() {
        return out_time;
    }

    public void setOut_time(String out_time) {
        this.out_time = out_time;
    }

    public String getReturn_time() {
        return return_time;
    }

    public void setReturn_time(String return_time) {
        this.return_time = return_time;
    }

    public String getGate_in_time() {
        return gate_in_time;
    }

    public void setGate_in_time(String gate_in_time) {
        this.gate_in_time = gate_in_time;
    }

    public String getGate_out_time() {
        return gate_out_time;
    }

    public void setGate_out_time(String gate_out_time) {
        this.gate_out_time = gate_out_time;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getApproved_by() {
        return approved_by;
    }

    public void setApproved_by(String approved_by) {
        this.approved_by = approved_by;
    }

    public String getApproved_date() {
        return approved_date;
    }

    public void setApproved_date(String approved_date) {
        this.approved_date = approved_date;
    }

    public String getRemarks(){return  remarks;}

    public void setRemarks(String remarks){this.remarks = remarks;}
}
