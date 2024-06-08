package com.catalogger.services;
import org.ehcache.CacheManager;
import org.ehcache.Cache;
import org.ehcache.config.builders.CacheConfigurationBuilder;
import org.ehcache.config.builders.ResourcePoolsBuilder;
import org.ehcache.config.builders.CacheManagerBuilder;
import org.ehcache.expiry.Expirations;
import java.util.concurrent.TimeUnit;
import org.ehcache.expiry.Duration;
import com.catalogger.models.Title;
import com.catalogger.models.Author;
import com.catalogger.models.Publisher;

public class CacheService {
	private static CacheService instance = null;
	private CacheManager manager;
    private Cache<Integer, Title> titleCache;
	private Cache<Integer, Author> authorCache;
	private Cache<Integer, Publisher> publisherCache;

	private CacheService() {
		this.manager = CacheManagerBuilder.newCacheManagerBuilder().build();
        this.manager.init();
		this.titleCache   = this.manager.createCache("titles", CacheConfigurationBuilder.newCacheConfigurationBuilder(Integer.class, Title.class, ResourcePoolsBuilder.heap(2))
			.withExpiry(Expirations.timeToLiveExpiration(Duration.of(10, TimeUnit.SECONDS))));
		this.authorCache   = this.manager.createCache("authors", CacheConfigurationBuilder.newCacheConfigurationBuilder(Integer.class, Author.class, ResourcePoolsBuilder.heap(2))
            .withExpiry(Expirations.timeToLiveExpiration(Duration.of(10, TimeUnit.SECONDS))));
		this.publisherCache   = this.manager.createCache("publishers", CacheConfigurationBuilder.newCacheConfigurationBuilder(Integer.class, Publisher.class, ResourcePoolsBuilder.heap(2))
            .withExpiry(Expirations.timeToLiveExpiration(Duration.of(10, TimeUnit.SECONDS))));
	}

	public static CacheService getInstance() {
		if (CacheService.instance == null) {
			CacheService.instance = new CacheService();
		}
		return CacheService.instance;
	}

	public Cache<Integer, Title> getTitleCache() {
		return this.titleCache;
	}

	public Cache<Integer, Author> getAuthorCache() {
		return this.authorCache;
	}

	public Cache<Integer, Publisher> getPublisherCache() {
		return this.publisherCache;
	}

	public void updateTitleCache(int id, Title title) {
		this.titleCache.put(id, title);
	}

	public void updateAuthorCache(int id, Author author) {
        this.authorCache.put(id, author);
    }

	public void updatePublisherCache(int id, Publisher publisher) {
        this.publisherCache.put(id, publisher);
    }

	public boolean containsTitle(int id) {
		return this.titleCache.containsKey(id);
	}

	public boolean containsAuthor(int id) {
        return this.authorCache.containsKey(id);
    }

	public boolean containsPublisher(int id) {
        return this.publisherCache.containsKey(id);
    }

	public Title getTitle(int id) {
		return this.titleCache.get(id);
	}

	public Author getAuthor(int id) {
        return this.authorCache.get(id);
    }

	public Publisher getPublisher(int id) {
        return this.publisherCache.get(id);
    }
}

