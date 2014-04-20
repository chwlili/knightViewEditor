package org.game.editors;
import java.io.File;

import org.eclipse.swt.SWT;
import org.eclipse.swt.SWTError;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.ole.win32.OLE;
import org.eclipse.swt.ole.win32.OleAutomation;
import org.eclipse.swt.ole.win32.OleControlSite;
import org.eclipse.swt.ole.win32.OleFrame;
import org.eclipse.swt.ole.win32.Variant;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;

public class Swf extends Composite
{
	private OleFrame frame;
	private OleAutomation player;

	/**
	 * Create the composite
	 * 
	 * @param parent
	 * @param style
	 */
	public Swf(Composite parent, int style)
	{
		super(parent, style);
		final FillLayout fillLayout = new FillLayout();
		setLayout(fillLayout);
		setSize(649, 60);
		createContents();
	}

	protected void createContents() 
	{  
		frame = new OleFrame(this, SWT.NO_TRIM);  
		frame.setLayout(new FillLayout());    
		
		OleControlSite controlSite;
		try 
		{
			controlSite = new OleControlSite(frame, SWT.NO_TRIM, "{D27CDB6E-AE6D-11cf-96B8-444553540000}");  
			controlSite.setForeground(Display.getCurrent().getSystemColor(SWT.COLOR_BLACK));  
			controlSite.setRedraw(true);  
			controlSite.setLayoutDeferred(true);  
			controlSite.doVerb(OLE.OLEIVERB_INPLACEACTIVATE);  
			controlSite.pack();  
		}
		catch (SWTError e) 
		{
			System.out.println("Unable to open activeX control");  
			return;  
		}  
		
		player = new OleAutomation(controlSite);
		setFocus(); // prevents vm from crashing on shutdown.  
		pack();
		
		//loadFile(Application.getConfigAsString(¡±media.list¡±));
		setQuality2("high");  
		setScale("noScale");  
		setWMode("Transparent");  
		setLoop(true);  
		play(true);  
	}

	public void setWMode(String s)
	{
		int ids[] = player.getIDsOfNames(new String[] { "WMode" });
		if (ids != null)
		{
			player.setProperty(ids[0], new Variant(s));
		}
	}

	public void setScaleMode(String s)
	{
		int ids[] = player.getIDsOfNames(new String[] { "ScaleMode" });
		if (ids != null)
		{
			player.setProperty(ids[0], new Variant(s));
		}
	}

	public void setScale(String s)
	{
		int ids[] = player.getIDsOfNames(new String[] { "Scale" });
		if (ids != null)
		{
			player.setProperty(ids[0], new Variant(s));
		}
	}

	public void setQuality2(String s)
	{
		int ids[] = player.getIDsOfNames(new String[] { "Quality2" });
		if (ids != null)
		{
			player.setProperty(ids[0], new Variant(s));
		}
	}

	public void play(boolean s)
	{
		int ids[] = player.getIDsOfNames(new String[] { "Playing" });
		if (ids != null)
		{
			System.out.println("playing");
			player.setProperty(ids[0], new Variant(s));
		}
	}

	public void loadMovie(File url)
	{
		int ids[] = player.getIDsOfNames(new String[] { "Movie" });
		if (ids != null)
		{
			player.setProperty(ids[0], new Variant(url.getAbsolutePath()));
		}
	}

	public void setLoop(boolean b)
	{
		int ids[] = player.getIDsOfNames(new String[] { "Loop" });
		if (ids != null)
		{
			player.setProperty(ids[0], new Variant(b));
		}
	}

	@Override
	protected void checkSubclass()
	{
	}
}
