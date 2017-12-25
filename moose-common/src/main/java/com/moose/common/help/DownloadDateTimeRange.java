package com.moose.common.help;

import com.google.common.collect.Lists;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class DownloadDateTimeRange {

    private LocalDateTime startAt;
    private LocalDateTime endAt;

    public DownloadDateTimeRange(LocalDateTime startAt, LocalDateTime endAt) {
        this.startAt = startAt;
        this.endAt = endAt;
    }

    public List<DownloadDateTimeRange> splitDownloadDate(int splitMinutesInterval) {
        if (!startAt.isBefore(endAt)) {
            throw new IllegalArgumentException("startAt invalid, must be earlier than endAt");
        }
        if(getMinutesInterval() < splitMinutesInterval) {
            return Lists.newArrayList(this);
        }
        return IntStream.range(0, getSplitQuantity(splitMinutesInterval))
                .mapToObj(i -> {
                    LocalDateTime splitStartAt = startAt.plusMinutes(i * splitMinutesInterval);
                    LocalDateTime splitEndAt = min(splitStartAt.plusMinutes(splitMinutesInterval), endAt);
                    return new DownloadDateTimeRange(splitStartAt, splitEndAt);
                })
                .collect(Collectors.toList());
    }

    private int getSplitQuantity(int splitMinutesInterval) {
        return (int) Math.ceil((double) getMinutesInterval() / (double) splitMinutesInterval);
    }

    public long getMinutesInterval() {
        return ChronoUnit.MINUTES.between(startAt, endAt);
    }

    private LocalDateTime min(LocalDateTime dateTime, LocalDateTime another) {
        return dateTime.isBefore(another) ? dateTime : another;
    }

    public LocalDateTime getStartAt() {
        return startAt;
    }

    public LocalDateTime getEndAt() {
        return endAt;
    }

}
