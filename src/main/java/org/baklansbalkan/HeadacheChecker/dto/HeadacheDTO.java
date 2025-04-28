package org.baklansbalkan.HeadacheChecker.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import org.baklansbalkan.HeadacheChecker.models.Localisation;
import org.baklansbalkan.HeadacheChecker.models.TimesOfDay;
import org.baklansbalkan.HeadacheChecker.util.ValueOfEnum;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class HeadacheDTO {

    private int id;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate date;

    private boolean isHeadache;

    private boolean isMedicine;

    @Size(max = 50, message = "Please, use no more than 50 characters")
    private String medicine;

    @Min(value = 0, message = "Intensity should be from 0 to 5")
    @Max(value = 5, message = "Intensity should be from 0 to 5")
    private int intensity;

    @Enumerated(EnumType.STRING)
    @ValueOfEnum(enumClass = Localisation.class, message = "Please, use one of the options: LEFT, RIGHT, ALL")
    private String localisation;

    @Enumerated(EnumType.STRING)
    @ValueOfEnum(enumClass = TimesOfDay.class, message = "Please, use one of the options: MORNING, AFTERNOON, EVENING, NIGHT")
    private String timesOfDay;

    @Size(max = 200, message = "Please, use no more than 200 characters")
    private String comment;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime createdAt;

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

    public String getLocalisation() {
        return localisation;
    }

    public void setLocalisation(String localisation) {
        this.localisation = localisation;
    }

    public String getTimesOfDay() {
        return timesOfDay;
    }

    public void setTimesOfDay(String timesOfDay) {
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
}
