package com.cacheMoney.services;
import com.cacheMoney.models.Transaction;
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
    private Cache<Integer, Transaction[]> cache;

	private CacheService() {
		this.manager = CacheManagerBuilder.newCacheManagerBuilder().build();
        this.manager.init();
		this.cache   = this.manager.createCache("transactions", CacheConfigurationBuilder.newCacheConfigurationBuilder(Integer.class, Transaction[].class, ResourcePoolsBuilder.heap(2))
			.withExpiry(Expirations.timeToLiveExpiration(Duration.of(10, TimeUnit.SECONDS))));
	}

	public static CacheService getInstance() {
		if (CacheService.instance == null) {
			CacheService.instance = new CacheService();
		}
		return CacheService.instance;
	}

	public Cache<Integer, Transaction[]> getCache() {
		return this.cache;
	}

	public void updateCache(int accountId, Transaction[] transactions) {
		this.cache.put(accountId, transactions);
	}

	public boolean contains(int accountId) {
		return this.cache.containsKey(accountId);
	}

	public Transaction[] getTransactions(int accountId) {
		return this.cache.get(accountId);
	}
}
