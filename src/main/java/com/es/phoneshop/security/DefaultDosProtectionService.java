package com.es.phoneshop.security;

import java.util.Date;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class DefaultDosProtectionService implements DosProtectionService {
    private static final long THRESHOLD = 20;
    private static final long MILLISECONDS_IN_MINUTE = 1000 * 60;
    private Map<String, Long> countMap;
    private Map<String, Date> lastRequestMap;

    private static DefaultDosProtectionService instance;

    private DefaultDosProtectionService() {
        countMap = new ConcurrentHashMap<>();
        lastRequestMap = new ConcurrentHashMap<>();
    }

    public static synchronized DefaultDosProtectionService getInstance() {
        if (instance == null) {
            instance = new DefaultDosProtectionService();
        }
        return instance;
    }

    @Override
    public boolean isAllowed(String ip) {
        Long count = countMap.get(ip);
        Date lastRequest = lastRequestMap.get(ip);
        if (lastRequest == null) {
            lastRequest = new Date();
        }
        if (count == null) {
            count = 1L;
        } else {
            if (isTimeUp(lastRequest)) {
                count = 0L;
                lastRequest = new Date();
            }
            if (count > THRESHOLD) {
                return false;
            }
            count++;
        }

        countMap.put(ip, count);
        lastRequestMap.put(ip, lastRequest);
        return true;
    }

    private boolean isTimeUp(Date lastRequest) {
        Date now = new Date();
        return now.after(new Date(lastRequest.getTime() + MILLISECONDS_IN_MINUTE));
    }
}
