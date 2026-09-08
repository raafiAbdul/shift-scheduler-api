package com.projects.shift_scheduler_api.dtos;

import jakarta.validation.constraints.Size;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;

public class ShiftDateDto {
    @Size(min = 1, max = 12) private int month;
    @Size(min = 1, max = 31) private int day;
    @Size(max = 23) private int hour;
    @Size(max = 59) private int minute;

    public ShiftDateDto() {
    }

    public ShiftDateDto(int month, int day, int hour, int minute) {
        this.month = month;
        this.day = day;
        this.hour = hour;
        this.minute = minute;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public int getHour() {
        return hour;
    }

    public void setHour(int hour) {
        this.hour = hour;
    }

    public int getMinute() {
        return minute;
    }

    public void setMinute(int minute) {
        this.minute = minute;
    }

    public static double differenceInHours(ShiftDateDto later, ShiftDateDto earlier) {
        OffsetDateTime laterODT = mapShiftDateDtoToOffsetDateTime(later);
        OffsetDateTime earlierODT = mapShiftDateDtoToOffsetDateTime(earlier);

        double laterHoursInDays = later.getHour() / 24.00;
        double earlierHoursInDays = earlier.getHour() / 24.00;
        double laterDays = laterODT.getDayOfYear() + laterHoursInDays;
        double earlierDays = earlierODT.getDayOfYear() + earlierHoursInDays;

        double difference = (laterDays - earlierDays) * 24;

        return (int)(difference * 100) / 100.00;
    }

    private static OffsetDateTime mapShiftDateDtoToOffsetDateTime(ShiftDateDto shiftDateDto) {
        return OffsetDateTime.of(
                OffsetDateTime.now().getYear(),
                shiftDateDto.getMonth(),
                shiftDateDto.getDay(),
                shiftDateDto.getHour(),
                shiftDateDto.getMinute(),
                0, 0,
                ZoneOffset.UTC
        );
    }
}
