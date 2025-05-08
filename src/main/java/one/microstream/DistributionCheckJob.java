package one.microstream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.micronaut.scheduling.annotation.Scheduled;
import jakarta.inject.Singleton;
import one.microstream.enterprise.cluster.nodelibrary.common.ClusterStorageManager;
import one.microstream.enterprise.cluster.nodelibrary.common.impl._default.NodeDefaultClusterStorageManager;

@Singleton
public class DistributionCheckJob
{
	private static final Logger LOG = LoggerFactory.getLogger(DistributionCheckJob.class);

	@Scheduled(fixedDelay = "5s")
	void checkDistribution(final ClusterStorageManager<?> storage)
	{
		// other managers like the local or backup do not support this check
		if (storage instanceof NodeDefaultClusterStorageManager)
		{
			LOG.info("Distribution: {}", storage.isDistributor());
		}
	}
}
