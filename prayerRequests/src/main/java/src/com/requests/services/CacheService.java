package com.requests.services;
import com.requests.models.PrayerRequest;
import org.ehcache.CacheManager;
import org.ehcache.Cache;
import org.ehcache.config.builders.CacheConfigurationBuilder;
import org.ehcache.config.builders.ResourcePoolsBuilder;
import org.ehcache.config.builders.CacheManagerBuilder;
import org.ehcache.expiry.Expirations;
import java.util.concurrent.TimeUnit;
import org.ehcache.expiry.Duration;

public class CacheService {
	private static CacheService instance = null;
	private CacheManager manager;
    private Cache<Integer, PrayerRequest[]> cache;

	private CacheService() {
		this.manager = CacheManagerBuilder.newCacheManagerBuilder().build();
        this.manager.init();
		this.cache   = this.manager.createCache("requests", CacheConfigurationBuilder.newCacheConfigurationBuilder(Integer.class, PrayerRequest[].class, ResourcePoolsBuilder.heap(2))
			.withExpiry(Expirations.timeToLiveExpiration(Duration.of(10, TimeUnit.SECONDS))));
	}

	public static CacheService getInstance() {
		if (CacheService.instance == null) {
			CacheService.instance = new CacheService();
		}
		return CacheService.instance;
	}

	public Cache<Integer, PrayerRequest[]> getCache() {
		return this.cache;
	}

	public void updateCache(int userId, PrayerRequest[] request) {
		this.cache.put(userId, request);
	}

	public boolean contains(int userId) {
		return this.cache.containsKey(userId);
	}

	public PrayerRequest[] getPrayerRequests(int userId) {
		return this.cache.get(userId);
	}
}
