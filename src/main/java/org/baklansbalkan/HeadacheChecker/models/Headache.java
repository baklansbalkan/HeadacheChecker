package org.baklansbalkan.HeadacheChecker.models;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.time.LocalDate;
import java.util.Objects;

@Entity
@Table(name = "Headache")
public class Headache {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "date")
    @Temporal(TemporalType.DATE)
    private LocalDate date;

    @Column(name = "isheadache")
    private boolean isHeadache;

    @Column(name = "ismedicine")
    private boolean isMedicine;

    @Column(name = "medicine")
    private String medicine;

    @Column(name = "intensity")
    private int intensity;

    @Column(name = "localisation")
    @Enumerated(EnumType.STRING)
    private Localisation localisation;

    @Column(name = "timesofday")
    @Enumerated(EnumType.STRING)
    private TimesOfDay timesOfDay;

    @Column(name = "comment")
    private String comment;

    @Column(name = "createdat")
    private LocalDateTime createdAt;

    public Headache(int id, LocalDate date, boolean isHeadache, boolean isMedicine, String medicine, int intensity, Localisation localisation, TimesOfDay timesOfDay, String comment) {
        this.id = id;
        this.date = date;
        this.isHeadache = isHeadache;
        this.isMedicine = isMedicine;
        this.medicine = medicine;
        this.intensity = intensity;
        this.localisation = localisation;
        this.timesOfDay = timesOfDay;
        this.comment = comment;
    }

    public Headache() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public boolean isIsHeadache() {
        return isHeadache;
    }

    public void setIsHeadache(boolean headache) {
        isHeadache = headache;
    }

    public boolean isIsMedicine() {
        return isMedicine;
    }

    public void setIsMedicine(boolean medicine) {
        isMedicine = medicine;
    }

    public String getMedicine() {
        return medicine;
    }

    public void setMedicine(String medicine) {
        this.medicine = medicine;
    }

    public int getIntensity() {
        return intensity;
    }

    public void setIntensity(int intensity) {
        this.intensity = intensity;
    }

    public Localisation getLocalisation() {
        return localisation;
    }

    public void setLocalisation(Localisation localisation) {
        this.localisation = localisation;
    }

    public TimesOfDay getTimesOfDay() {
        return timesOfDay;
    }

    public void setTimesOfDay(TimesOfDay timesOfDay) {
        this.timesOfDay = timesOfDay;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Headache headache = (Headache) o;
        return id == headache.id && isHeadache == headache.isHeadache && isMedicine == headache.isMedicine && intensity == headache.intensity && Objects.equals(date, headache.date) && Objects.equals(medicine, headache.medicine) && localisation == headache.localisation && timesOfDay == headache.timesOfDay && Objects.equals(comment, headache.comment);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, date, isHeadache, isMedicine, medicine, intensity, localisation, timesOfDay, comment);
    }

    @Override
    public String toString() {
        return "Headache{" +
                "id=" + id +
                ", date=" + date +
                ", isHeadache=" + isHeadache +
                ", isMedicine=" + isMedicine +
                ", medicine='" + medicine + '\'' +
                ", intensity=" + intensity +
                ", localisation=" + localisation +
                ", timesOfDay=" + timesOfDay +
                ", comment='" + comment + '\'' +
                '}';
    }
}




