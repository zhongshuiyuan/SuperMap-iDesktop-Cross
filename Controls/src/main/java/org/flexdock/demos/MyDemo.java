package org.flexdock.demos;

import org.flexdock.docking.Dockable;
import org.flexdock.docking.DockingConstants;
import org.flexdock.docking.DockingManager;
import org.flexdock.docking.defaults.DefaultDockingStrategy;
import org.flexdock.docking.event.DockingEvent;
import org.flexdock.docking.event.DockingListener;
import org.flexdock.docking.state.PersistenceException;
import org.flexdock.util.SwingUtility;
import org.flexdock.view.View;
import org.flexdock.view.Viewport;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.event.AncestorEvent;
import javax.swing.event.AncestorListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.io.IOException;

/**
 * Created by highsad on 2016/11/11.
 */
public class MyDemo extends JFrame {
	public static void main(String[] args) {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (UnsupportedLookAndFeelException e) {
			e.printStackTrace();
		}
		final MyDemo demo = new MyDemo();
		demo.setSize(600, 400);
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				demo.setVisible(true);
			}
		});
	}

	public MyDemo() {
		Viewport viewport = new Viewport();
		this.getContentPane().setLayout(new BorderLayout());
		JButton button = new JButton("可见控制");

//		this.getContentPane().add(button, BorderLayout.NORTH);
		this.getContentPane().add(viewport, BorderLayout.CENTER);

		JPanel panelMain = new JPanel();
		panelMain.setBackground(Color.lightGray);
		final View mainView = new View("main", "main");
		mainView.setContentPane(panelMain);
		mainView.setTitlebar(null);
		mainView.setTerritoryBlocked(DockingConstants.CENTER_REGION, true);
		JPanel panel1 = createPanel("panel1");
		JPanel panel2 = createPanel("panel2");
		JPanel panel3 = createPanel("panel3");
		JPanel panel4 = createPanel("panel4");

		final View view1 = new View("panel1", "panel1");
		view1.setTerritoryBlocked(DockingConstants.WEST_REGION, true);
		view1.setTerritoryBlocked(DockingConstants.EAST_REGION, true);
		view1.setTerritoryBlocked(DockingConstants.CENTER_REGION, true);
		view1.setContentPane(panel1);

		View view2 = new View("panel2", "panel2");
		view2.setContentPane(panel2);

		View view3 = new View("panel3", "panel3");
		view3.setContentPane(panel3);

		View view4 = new View("panel4", "panel4");
		view4.setContentPane(panel4);
		viewport.dock(mainView);

//		viewport.setRegionBlocked(DockingConstants.CENTER_REGION, true);
//		viewport.dock(((Dockable) view3), DockingConstants.EAST_REGION);
//		viewport.dock((Dockable) view1, DockingConstants.SOUTH_REGION);
//		viewport.dock(((Dockable) view2), DockingConstants.WEST_REGION);

		DefaultDockingStrategy.keepConstantPercentage(true);

		mainView.dock((Dockable) view2, DockingConstants.WEST_REGION, 0.5f);
		view2.dock((Dockable) view3, DockingConstants.SOUTH_REGION, 0.5f);
		mainView.dock((Dockable) view1, DockingConstants.SOUTH_REGION, 0.5f);

		view2.addAncestorListener(new AncestorListener() {
			@Override
			public void ancestorAdded(AncestorEvent event) {
				System.out.println("");
			}

			@Override
			public void ancestorRemoved(AncestorEvent event) {

			}

			@Override
			public void ancestorMoved(AncestorEvent event) {

			}
		});

		view2.addDockingListener(new DockingListener() {
			@Override
			public void dockingComplete(DockingEvent evt) {
				System.out.println("");
			}

			@Override
			public void dockingCanceled(DockingEvent evt) {

			}

			@Override
			public void dragStarted(DockingEvent evt) {
				System.out.println("");
			}

			@Override
			public void dropStarted(DockingEvent evt) {

			}

			@Override
			public void undockingComplete(DockingEvent evt) {

			}

			@Override
			public void undockingStarted(DockingEvent evt) {

			}
		});

//		view.dock((Dockable) view2, DockingConstants.CENTER_REGION);
//		view.dock((Dockable) view3, DockingConstants.CENTER_REGION);
//		view.dock((Dockable) view1, DockingConstants.CENTER_REGION);

		button.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (DockingManager.isDocked((Dockable) view1)) {
					DockingManager.close(view1);
				} else {
					DockingManager.display(view1);
				}
//				System.out.println(DockingManager.getDockingState(view1).toString());
			}
		});
	}

	private JPanel createPanel(String title) {
		JPanel panel = new JPanel();
		panel.setLayout(new BorderLayout());
		panel.add(new JButton(title), BorderLayout.CENTER);
		return panel;
	}
}
