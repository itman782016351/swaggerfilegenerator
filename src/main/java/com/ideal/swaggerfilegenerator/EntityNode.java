package com.ideal.swaggerfilegenerator;

/**
 * @author zhaopei
 * @create 2019-09-20 11:21
 */
public class EntityNode {
    private String orderNo;
    private String parentOrderNo;
    private String propertyName;
    private boolean leafNode;
    private String desc;
    private boolean collection;
    private boolean required;

    public EntityNode(String cell1Val, String cell2Val,boolean cell3Val, String cell4Val, boolean cell5Val) {
        this.orderNo=cell1Val;
        this.propertyName=cell2Val;
        this.collection=cell3Val;
        this.desc=cell4Val;
        this.required=cell5Val;
        this.parentOrderNo=cell1Val.lastIndexOf(".")<0?"":cell1Val.substring(0,cell1Val.lastIndexOf("."));
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public String getParentOrderNo() {
        return parentOrderNo;
    }

    public void setParentOrderNo(String parentOrderNo) {
        this.parentOrderNo = parentOrderNo;
    }

    public String getPropertyName() {
        return propertyName;
    }

    public void setPropertyName(String propertyName) {
        this.propertyName = propertyName;
    }

    public boolean isLeafNode() {
        return leafNode;
    }

    public void setLeafNode(boolean leafNode) {
        this.leafNode = leafNode;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public boolean isCollection() {
        return collection;
    }

    public void setCollection(boolean collection) {
        this.collection = collection;
    }

    public boolean isRequired() {
        return required;
    }

    public void setRequired(boolean required) {
        this.required = required;
    }
}
