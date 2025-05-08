package one.microstream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.micronaut.scheduling.annotation.Scheduled;
import jakarta.inject.Singleton;
import one.microstream.enterprise.cluster.nodelibrary.common.ClusterStorageManager;

@Singleton
public class DistributionCheckJob
{
	private static final Logger LOG = LoggerFactory.getLogger(DistributionCheckJob.class);

	@Scheduled(fixedDelay = "5s")
	void checkDistribution(final ClusterStorageManager<?> storage)
	{
		LOG.info("Distribution: {}", storage.isDistributor());
	}
}
