package org.example;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class Main {
    private static final Map<YearMonth, StartEndTimePair> MILLIS_MAP = new HashMap<>();

    private static final int BASE_YEAR = 1970;
    private static final int LAST_YEAR = 2100;
    private static final int YEARS = LAST_YEAR - BASE_YEAR + 1;
    private static final int SIZE = YEARS * 12 + 1; // +1 for the start of the month beyond the last year
    private static final long[] MONTH_START = new long[SIZE];
    public static final ZoneId ZONE = ZoneId.systemDefault();

    static {
        initMillisMap();
        initMonthStartArray();
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

    private static void initMonthStartArray() {
        int idx = 0;
        for (int y = BASE_YEAR; y <= LAST_YEAR; y++) { // Loop through each year
            for (int m = 1; m <= 12; m++) { // Loop through each month
                MONTH_START[idx++] = LocalDate.of(y, m, 1)
                        .atStartOfDay(ZONE)
                        .toInstant()
                        .toEpochMilli();
            }
        }
        MONTH_START[idx] = LocalDate.of(LAST_YEAR, 12, 1)
                .plusMonths(1)
                .atStartOfDay(ZONE)
                .toInstant()
                .toEpochMilli(); // Add the start of the month beyond the last year
    }

    public static long getStartTimeInMillis(int year, int month) {
        int i = index(year, month);
        return MONTH_START[i] - 1L;
    }

    public static long getEndTimeInMillis(int year, int month) {
        int i = index(year, month);
        return MONTH_START[i + 1] - 1L;
    }

    private static int index(int year, int month) {
        if (year < BASE_YEAR || year > LAST_YEAR || month < 1 || month > 12) {
            throw new IllegalArgumentException("Year must be between " + BASE_YEAR + " and " + LAST_YEAR + ", and month must be between 1 and 12.");
        }
        return (year - BASE_YEAR) * 12 + (month - 1);
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

    public static Result createAndObjUsingMemoizationInMap(int aNumber, int aYear, int aMonth) {
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

    public static Result createAnObjNewJavaTimeAPIUsingMillisMath(int aNumber, int aYear, int aMonth) {
        YearMonth yearMonth = YearMonth.of(aYear, aMonth);
        long startTime = yearMonth.atDay(1).atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli() - 1;
        long endTime = yearMonth.plusMonths(1).atDay(1).atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli() - 1;

        return new Result(aNumber, startTime, endTime);
    }

    public static Result createAnObjUsingMemoizationInArray(int aNumber, int aYear, int aMonth) {
        long startTime = getStartTimeInMillis(aYear, aMonth);
        long endTime = getEndTimeInMillis(aYear, aMonth);
        return new Result(aNumber, startTime, endTime);
    }


    public record Result(int number, long startTime, long endtime) {}
    public record StartEndTimePair(long startTime, long endTime) {}
}