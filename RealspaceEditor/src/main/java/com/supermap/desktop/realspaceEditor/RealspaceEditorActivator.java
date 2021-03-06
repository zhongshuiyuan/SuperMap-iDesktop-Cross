package com.supermap.desktop.realspaceEditor;

import com.supermap.desktop.Application;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

public class RealspaceEditorActivator implements BundleActivator {

	private static BundleContext CONTEXT;

	static BundleContext getContext() {
		return CONTEXT;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.osgi.framework.BundleActivator#start(org.osgi.framework.BundleContext)
	 */
	@Override
	public void start(BundleContext bundleContext) throws Exception {
		System.out.println("Hello SuperMap === RealspaceEditor!!");

		RealspaceEditorActivator.setContext(bundleContext);

		Application.getActiveApplication().getPluginManager().addPlugin("SuperMap.Desktop.RealspaceEditor", bundleContext.getBundle());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.osgi.framework.BundleActivator#stop(org.osgi.framework.BundleContext)
	 */
	@Override
	public void stop(BundleContext bundleContext) throws Exception {
		RealspaceEditorActivator.setContext(null);
		System.out.println("Goodbye SuperMap === RealspaceEditor!!");
	}

	private static void setContext(BundleContext bundleContext) {
		RealspaceEditorActivator.CONTEXT = bundleContext;
	}

}
