package org.pentaho.reporting.platform.plugin;

import java.io.File;

import junit.framework.TestCase;
import org.pentaho.platform.api.engine.IPentahoDefinableObjectFactory;
import org.pentaho.platform.api.engine.IPentahoSession;
import org.pentaho.platform.api.engine.IPluginProvider;
import org.pentaho.platform.api.engine.IServiceManager;
import org.pentaho.platform.api.engine.ISolutionEngine;
import org.pentaho.platform.api.engine.IUserRoleListService;
import org.pentaho.platform.api.repository2.unified.IUnifiedRepository;
import org.pentaho.platform.engine.core.system.PentahoSessionHolder;
import org.pentaho.platform.engine.core.system.StandaloneSession;
import org.pentaho.platform.engine.security.userrole.ws.MockUserRoleListService;
import org.pentaho.platform.engine.services.solution.SolutionEngine;
import org.pentaho.platform.plugin.services.pluginmgr.SystemPathXmlPluginProvider;
import org.pentaho.platform.plugin.services.pluginmgr.servicemgr.DefaultServiceManager;
import org.pentaho.platform.repository2.unified.fs.FileSystemBackedUnifiedRepository;
import org.pentaho.reporting.platform.plugin.repository.PentahoNameGenerator;
import org.pentaho.reporting.platform.plugin.repository.TempDirectoryNameGenerator;
import org.pentaho.test.platform.engine.core.MicroPlatform;

public class ParameterTest extends TestCase
{
  private MicroPlatform microPlatform;

  public ParameterTest()
  {

  }

  @Override
  protected void setUp() throws Exception {
    new File("./resource/solution/system/tmp").mkdirs();

    microPlatform = new MicroPlatform("./resource/solution"); //$NON-NLS-1$
    microPlatform.define(ISolutionEngine.class, SolutionEngine.class);
    microPlatform.define(IUnifiedRepository.class, FileSystemBackedUnifiedRepository.class);
    microPlatform.define(IPluginProvider.class, SystemPathXmlPluginProvider.class);
    microPlatform.define(IServiceManager.class, DefaultServiceManager.class, IPentahoDefinableObjectFactory.Scope.GLOBAL);
    microPlatform.define(PentahoNameGenerator.class, TempDirectoryNameGenerator.class, IPentahoDefinableObjectFactory.Scope.GLOBAL);
    microPlatform.define(IUserRoleListService.class, MockUserRoleListService.class);
    microPlatform.start();
    IPentahoSession session = new StandaloneSession("test user");
    PentahoSessionHolder.setSession(session);
  }

  @Override
  protected void tearDown() throws Exception {
    microPlatform.stop();
  }

  public void testParameterProcessing() throws Exception
  {
    final ParameterContentGenerator contentGenerator = new ParameterContentGenerator();
    final ParameterXmlContentHandler handler = new ParameterXmlContentHandler(contentGenerator);
    handler.createParameterContent(System.out, "resource/solution/test/reporting/Product Sales.prpt", false);
  }

}
