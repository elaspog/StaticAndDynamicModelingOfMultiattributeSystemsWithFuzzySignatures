package net.prokhyon.modularfuzzy.shell.services;

import net.prokhyon.modularfuzzy.common.CommonServices;

public class ServiceFactory {

	public CommonServices getCommonServices() {

		return CommonServicesImplSingleton.getInstance();
	}

	public ShellServices getShellServices() {

		return CommonServicesImplSingleton.getInstance();
	}

}
