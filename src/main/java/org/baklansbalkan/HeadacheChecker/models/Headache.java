package org.baklansbalkan.HeadacheChecker.models;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;
import java.time.LocalDate;
import java.util.Objects;

@Entity
@Table(name = "Headache")
public class Headache {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "date")
    @Temporal(TemporalType.DATE)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate date;

    @Column(name = "isheadache")
    private boolean isHeadache;

    @Column(name = "ismedicine")
    private boolean isMedicine;

    @Column(name = "medicine")
    @Size(max = 50, message = "Please, use no more than 50 characters")
    private String medicine;

    @Column(name = "intensity")
    @Min(value = 0, message = "Intensity should be from 0 to 5")
    @Max(value = 5, message = "Intensity should be from 0 to 5")
    private Integer intensity;

    @Column(name = "localisation")
    private Localisation localisation;

    @Column(name = "timesofday")
    private TimesOfDay timesOfDay;

    @Column(name = "comment")
    @Size(max = 200, message = "Please, use no more than 200 characters")
    private String comment;

    @Column(name = "createdat")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime createdAt;

    @Column(name = "userid", nullable = false)
    private Integer userId;

    public Headache(Integer id, LocalDate date, boolean isHeadache, boolean isMedicine, String medicine, Integer intensity, Localisation localisation, TimesOfDay timesOfDay, String comment, Integer userId) {
        this.id = id;
        this.date = date;
        this.isHeadache = isHeadache;
        this.isMedicine = isMedicine;
        this.medicine = medicine;
        this.intensity = intensity;
        this.localisation = localisation;
        this.timesOfDay = timesOfDay;
        this.comment = comment;
        this.userId = userId;
    }

    public Headache() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
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

    public Integer getIntensity() {
        return intensity;
    }

    public void setIntensity(Integer intensity) {
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

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
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
                ", createdAt=" + createdAt +
                ", userId=" + userId +
                '}';
    }
}




