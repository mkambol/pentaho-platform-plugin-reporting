package org.pentaho.reporting.platform.plugin.output;

import java.io.IOException;
import java.io.OutputStream;

import org.pentaho.reporting.engine.classic.core.MasterReport;
import org.pentaho.reporting.engine.classic.core.ReportProcessingException;
import org.pentaho.reporting.engine.classic.core.layout.output.YieldReportListener;
import org.pentaho.reporting.engine.classic.core.modules.output.pageable.base.PageableReportProcessor;
import org.pentaho.reporting.engine.classic.core.modules.output.pageable.plaintext.PageableTextOutputProcessor;
import org.pentaho.reporting.engine.classic.core.modules.output.pageable.plaintext.driver.TextFilePrinterDriver;
import org.pentaho.reporting.libraries.repository.ContentIOException;

public class PlainTextOutput implements ReportOutputHandler
{
  private ProxyOutputStream proxyOutputStream;

  public PlainTextOutput()
  {
  }

  public Object getReportLock()
  {
    return this;
  }

  public int paginate(MasterReport report, int yieldRate) throws ReportProcessingException, IOException, ContentIOException {
    return 0;
  }
  
  public int generate(final MasterReport report,
                          final int acceptedPage,
                          final OutputStream outputStream,
                          final int yieldRate) throws ReportProcessingException, IOException, ContentIOException
  {
    final PageableReportProcessor proc = create(report, yieldRate);
    proxyOutputStream.setParent(outputStream);
    try
    {
      if (proc.isPaginated() == false)
      {
        proc.paginate();
      }
      proc.processReport();
      return 0;
    }
    finally
    {
      proc.close();
      proxyOutputStream.setParent(null);
    }
  }

  private PageableReportProcessor create(final MasterReport report, final int yieldRate)
      throws ReportProcessingException
  {
    proxyOutputStream = new ProxyOutputStream();
    final TextFilePrinterDriver driver = new TextFilePrinterDriver(proxyOutputStream, 12, 6);
    final PageableTextOutputProcessor outputProcessor = new PageableTextOutputProcessor(driver, report.getConfiguration());
    final PageableReportProcessor proc = new PageableReportProcessor(report, outputProcessor);
    if (yieldRate > 0)
    {
      proc.addReportProgressListener(new YieldReportListener(yieldRate));
    }
    return proc;
  }

  public boolean supportsPagination() {
    return false;
  }

  public void close()
  {
  }
}
