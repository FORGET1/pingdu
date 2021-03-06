package com.pingdu.baseModel;


import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlTransient;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.map.annotate.JsonRootName;
import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * 要输出的JSON结构体对象
 * 
 */
//@XmlRootElement(name = "root")
// 不拼接NULL节点
@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
@JsonRootName(value = "root")
// 用Spring标记该组件root
@Component(value = "root")
@Scope("prototype")
public class Root implements Serializable {

	/**
	 * Default serialVersionUID
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 本次操作状态(-1/0/其他),默认值为{code:0,msg:""} <br>
	 * 0：正常<br>
	 * -1：失败<br>
	 * 其他：自定义值
	 */
	private Status status = new Status();

	/**
	 * 实体对象,包含一个Bean对象
	 */
	private BaseEntity baseEntity;
	/**
	 * 实体对象,包含一个map对象
	 */
	private Map<String,Object> map;

	
	/**
	 * 集合泛型实体对象,包含一个List集合
	 */
	private List<? extends BaseEntity> jsonBeanList;
	
	private Set<? extends BaseEntity> jsonBeanSet;

	/**
	 * 用于设置不同的对象,该对象不会输出JSON或者XML
	 */
	private Object object;
	
	private Integer pageCount;
	
	
	private Integer sumPage;
	private Integer currentPage;
	
	private String result;
	
	
	
	@JsonProperty(value = "result")
	@XmlElementRef
	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}

	@JsonProperty(value = "sumPage")
	@XmlElementRef
	public Integer getSumPage() {
		return sumPage;
	}

	public void setSumPage(Integer sumPage) {
		this.sumPage = sumPage;
	}

	@JsonProperty(value = "currentPage")
	@XmlElementRef
	public Integer getCurrentPage() {
		return currentPage;
	}

	public void setCurrentPage(Integer currentPage) {
		this.currentPage = currentPage;
	}

	@JsonProperty(value = "data")
	@XmlElementRef
	public BaseEntity getBaseEntity() {
		return baseEntity;
	}

	public void setBaseEntity(BaseEntity baseEntity) {
		this.baseEntity = baseEntity;
	}
	@JsonProperty(value = "mapdata")
	@XmlElementRef
	public Map<String, Object> getMap() {
		return map;
	}

	public void setMap(Map<String, Object> map) {
		this.map = map;
	}

	@JsonProperty("dataList")
	@XmlElementWrapper(name = "dataList")
	@XmlElementRef
	public List<? extends BaseEntity> getJsonBeanList() {
		return jsonBeanList;
	}

	public void setJsonBeanList(List<? extends BaseEntity> jsonBeanList) {
		this.jsonBeanList = jsonBeanList;
	}
	
	
	
	@JsonProperty("dataSet")
	@XmlElementWrapper(name = "dataSet")
	@XmlElementRef
	public Set<? extends BaseEntity> getJsonBeanSet() {
		return jsonBeanSet;
	}

	public void setJsonBeanSet(Set<? extends BaseEntity> jsonBeanSet) {
		this.jsonBeanSet = jsonBeanSet;
	}

	@JsonProperty("pageCount")
	@XmlElement(name = "pageCount", required = true)
	public Integer getPageCount() {
		return pageCount;
	}

	public void setPageCount(Integer pageCount) {
		this.pageCount = pageCount;
	}

	// 该节点不做XML和JSON映射
	@XmlTransient
	@JsonIgnore
	public Object getObject() {
		return object;
	}

	@SuppressWarnings("unchecked")
	public void setObject(Object object) {
		// this.object = object;
		if(object == null){
			this.baseEntity = null;
			this.jsonBeanList = null;
		}else if (object instanceof List<?>) {
			// List<? extends BaseEntity>类型对象
			this.jsonBeanList = (List<? extends BaseEntity>) object;// JSON集合
			this.jsonBeanSet = null;
			this.baseEntity = null;
			this.pageCount = null;
			this.map=null;
		} else if(object instanceof Set<?>){
			this.jsonBeanSet = (Set<? extends BaseEntity>) object;// JSON集合
			this.jsonBeanList = null;
			this.baseEntity = null;
			this.pageCount = null;
			this.map=null;
		}else if (object instanceof BaseEntity) {
			// BaseEntity类型对象
			this.baseEntity = (BaseEntity) object;// JSON对象
			this.jsonBeanList = null;
			this.jsonBeanSet = null;
			this.map=null;
		} else if (object instanceof String) {
			// String类型字符串
			this.status = new Status((String) object);
		} else if (object instanceof Integer) {
			// Integer类型字符串
			this.status = new Status((Integer) object, "");
		}else if(object instanceof Map ){
			// BaseObject类型对象
			this.map = (Map<String,Object>) object;// JSON对象
			this.jsonBeanList = null;
			this.jsonBeanSet = null;
			this.baseEntity=null;
			
		} else {
			System.out.println("root.setObject()传入一个不识别的类型,应该传入{String,BaseEntity,List<? extends BaseEntity>,Integer}中的类型");
			this.status = new Status(-1, "系统返回结果异常");
		}
	}

	@JsonProperty(value = "status")
	@XmlElement(name = "status", required = true)
	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	// /////////////////////////////////////自定义方法////////////////////////////////////////////////////
	/**
	 * 设置返回消息<br>
	 * 返回的状态码为0
	 * 
	 * @param msg 消息内容
	 */
	public void setMessage(String msg) {
		this.status = new Status(msg);
	}

	/**
	 * 设置返回状态码Code和消息Message<br>
	 * 返回的状态码为Code
	 * 
	 * @param code 状态码
	 * @param msg 消息内容
	 */
	public void setMessage(Integer code, String msg) {
		this.status = new Status(code, msg);
	}

	/**
	 * 设置状态码Code<br>
	 * 返回的消息内容为""
	 * 
	 * @param code 状态码
	 */
	public void setCode(Integer code) {
		this.status = new Status(code, "");
	}

	/**
	 * 设置返回Error消息<br>
	 * 返回的状态码为-1
	 * 
	 * @param msg 消息内容
	 */
	public void setError(String errorMsg) {
		this.status = new Status(-1, errorMsg);
	}
}
