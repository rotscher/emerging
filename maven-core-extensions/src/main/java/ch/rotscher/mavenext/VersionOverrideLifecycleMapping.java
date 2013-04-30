package ch.rotscher.mavenext;

import java.util.Map;

import org.apache.maven.lifecycle.mapping.DefaultLifecycleMapping;
import org.apache.maven.lifecycle.mapping.Lifecycle;

public class VersionOverrideLifecycleMapping extends DefaultLifecycleMapping {

	
	@Override
	public Map<String, Lifecycle> getLifecycles() {
		Map<String, Lifecycle> lifecycles = super.getLifecycles();
		
		if (VersionOverrideModelReader.isVersionOverridden()) {
			for (Lifecycle lifecycle : lifecycles.values()) {
				for (String phase : lifecycle.getPhases().keySet()) {
					if ("install".equals(phase)) {
						lifecycle.getPhases().put(phase, "ch.rotscher.maven.plugins:install-version_override-plugin:0.2.0-SNAPSHOT:install");
					}
				}
			}
		}
		
		return lifecycles;
	}
}
