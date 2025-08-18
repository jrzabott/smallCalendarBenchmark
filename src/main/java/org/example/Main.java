package org.example;

import java.time.YearMonth;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class Main {
    private static final Map<YearMonth, StartEndTimePair> MILLIS_MAP = new HashMap<>();
    static {
        initMillisMap();
    }

    private static void initMillisMap() {
        for (int i = 1970; i < 2100; i++) {
            for (int j = 1; j <= 12; j++) {
                YearMonth yearMonth = YearMonth.of(i, j);
                long startTime = yearMonth.minusMonths(1).atEndOfMonth().atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli();
                long endTime = yearMonth.atEndOfMonth().atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli();
                MILLIS_MAP.put(yearMonth, new StartEndTimePair(startTime, endTime));
            }
        }
    }

    public static Result createAnObjViaCalendar(int aNumber, int aYear, int aMonth) {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.DAY_OF_MONTH, 1);

        cal.set(Calendar.YEAR, aYear);
        cal.set(Calendar.MONTH, aMonth);

        cal.add(Calendar.DAY_OF_MONTH, -1);
        long startTime = cal.getTimeInMillis();
        cal.add(Calendar.DAY_OF_MONTH, 1);
        long endTime = cal.getTimeInMillis();

        return new Result(aNumber, startTime, endTime);
    }

    public static Result createAnObjViaNewJavaTimeAPI(int aNumber, int aYear, int aMonth) {
        YearMonth yearMonth = YearMonth.of(aYear, aMonth);
        long startTime = yearMonth.minusMonths(1).atEndOfMonth().atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli();
        long endTime = yearMonth.atEndOfMonth().atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli();

        return new Result(aNumber, startTime, endTime);
    }

    public static Result createAndObjUsingMemoization(int aNumber, int aYear, int aMonth) {
        long startTime = MILLIS_MAP.get(YearMonth.of(aYear, aMonth)).startTime();
        long endTime = MILLIS_MAP.get(YearMonth.of(aYear, aMonth)).endTime();
        return new Result(aNumber, startTime, endTime);
    }

    public static Result createAnObjNewJavaTimeAPIMinusNano(int aNumber, int aYear, int aMonth) {
        YearMonth yearMonth = YearMonth.of(aYear, aMonth);
        long startTime = yearMonth.atDay(1).atStartOfDay(ZoneId.systemDefault()).minusNanos(1).toInstant().toEpochMilli();
        long endTime = yearMonth.plusMonths(1).atDay(1).atStartOfDay(ZoneId.systemDefault()).minusNanos(1).toInstant().toEpochMilli();

        return new Result(aNumber, startTime, endTime);
    }

    public static Result createAnObjNewJavaTimeAPIMinusMillis(int aNumber, int aYear, int aMonth) {
        YearMonth yearMonth = YearMonth.of(aYear, aMonth);
        long startTime = yearMonth.atDay(1).atStartOfDay(ZoneId.systemDefault()).minus(1, ChronoUnit.MILLIS).toInstant().toEpochMilli();
        long endTime = yearMonth.plusMonths(1).atDay(1).atStartOfDay(ZoneId.systemDefault()).minus(1, ChronoUnit.MILLIS).toInstant().toEpochMilli();

        return new Result(aNumber, startTime, endTime);
    }


    public record Result(int number, long startTime, long endtime) {}
    public record StartEndTimePair(long startTime, long endTime) {}
}