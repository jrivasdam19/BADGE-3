package com.example.data;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.util.List;

@XmlRootElement(name = "Training")
@XmlAccessorType(XmlAccessType.FIELD)
public class Training implements Serializable {

//    @XmlElementWrapper(name = "formations")
    @XmlElement(name = "formation")
    private List<Student>formations;

    public List<Student> getFormations() {
        return formations;
    }

    public void setFormations(List<Student> formations) {
        this.formations = formations;
    }
}
