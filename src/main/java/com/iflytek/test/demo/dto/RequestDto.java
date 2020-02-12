package com.iflytek.test.demo.dto;

import javax.validation.constraints.NotNull;

public class RequestDto implements IRequest {


    private String frame;//音频字节

    private String sid;//音频id

    private Integer idx;//音频顺序

    private Integer isLast;//是否最后一包 1是0否


    private Integer sampleRate;//采样率 8为8k，16为16k


    public String getFrame() {
        return frame;
    }

    public void setFrame(String frame) {
        this.frame = frame;
    }

    public String getSid() {
        return sid;
    }

    public void setSid(String sid) {
        this.sid = sid;
    }

    public Integer getIdx() {
        return idx;
    }

    public void setIdx(Integer idx) {
        this.idx = idx;
    }

    public Integer getIsLast() {
        return isLast;
    }

    public void setIsLast(Integer isLast) {
        this.isLast = isLast;
    }

    public Integer getSampleRate() {
        return sampleRate;
    }

    public void setSampleRate(Integer sampleRate) {
        this.sampleRate = sampleRate;
    }


    @Override
    public String toString() {
        return "RequestDto{" +
                "frame='" + frame + '\'' +
                ", sid='" + sid + '\'' +
                ", idx=" + idx +
                ", isLast=" + isLast +
                ", sampleRate=" + sampleRate +
                '}';
    }

    @Override
    public void verify() {

    }
}
