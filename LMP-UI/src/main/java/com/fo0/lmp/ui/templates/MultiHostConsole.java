package com.fo0.lmp.ui.templates;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Future;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.vaadin.addons.ComboBoxMultiselect;
import org.vaadin.alump.materialicons.MaterialIcons;
import org.vaadin.viritin.button.MButton;
import org.vaadin.viritin.grid.MGrid;
import org.vaadin.viritin.layouts.MHorizontalLayout;
import org.vaadin.viritin.layouts.MPanel;

import com.fo0.lmp.ui.abstracts.AVerticalView;
import com.fo0.lmp.ui.enums.ELinuxActions;
import com.fo0.lmp.ui.enums.EWindowSize;
import com.fo0.lmp.ui.model.Host;
import com.fo0.lmp.ui.model.HostGridData;
import com.fo0.lmp.ui.model.MultiHostConnector;
import com.fo0.lmp.ui.model.SearchSuggestionProvider;
import com.fo0.lmp.ui.utils.STYLES;
import com.fo0.lmp.ui.utils.Utils;
import com.fo0.lmp.ui.utils.UtilsNotification;
import com.fo0.lmp.ui.utils.UtilsWindow;
import com.vaadin.event.ShortcutAction.KeyCode;
import com.vaadin.event.ShortcutAction.ModifierKey;
import com.vaadin.server.FontAwesome;
import com.vaadin.ui.Component;
import com.vaadin.ui.renderers.HtmlRenderer;
import com.vaadin.ui.themes.ValoTheme;

import eu.maxschuster.vaadin.autocompletetextfield.AutocompleteTextField;
import eu.maxschuster.vaadin.autocompletetextfield.shared.ScrollBehavior;

public class MultiHostConsole extends AVerticalView {

	private static final long serialVersionUID = 429811699084509611L;

	private Thread consoleThreadUI;
	private MultiHostConnector connector;

	private MGrid<HostGridData> gridHostStatus;
	private List<HostGridData> gridData = new ArrayList<HostGridData>();

	private HostConsole console;

	private ComboBoxMultiselect<Host> selectedHosts;
	private AutocompleteTextField searchfield;

	private Set<Host> hosts = new HashSet<Host>();
	private Set<String> actions = new HashSet<String>();

	private MButton start = null;
	private MButton stop = null;
	private MButton clear = null;

	private String[] initActions = null;

	private boolean inputfield = false;
	private boolean singleHost = false;

	public MultiHostConsole(Set<Host> hosts, String... action) {
		this(hosts, false, action);
	}

	public MultiHostConsole(Set<Host> hosts, boolean preSelect, String... action) {
		this(hosts, preSelect, false, false, action);
	}

	public MultiHostConsole(Set<Host> hosts, boolean preSelect, boolean start, boolean inputfield, boolean singleHost,
			String... action) {
		this.initActions = action;
		this.inputfield = inputfield;
		if (hosts != null)
			this.hosts.addAll(hosts);

		this.singleHost = singleHost;

		initBuild();

		if (preSelect) {
			selectedHosts.select(hosts.iterator().next());
			searchfield.setValue(Stream.of(action).iterator().next());
		}

		startAction(searchfield.getValue());
	}

	public MultiHostConsole(Set<Host> hosts, boolean preSelect, boolean start, boolean inputfield, String... action) {
		this(hosts, preSelect, start, inputfield, false, action);
	}

	public void startAction(String action) {
		gridData.clear();

		if (selectedHosts.getValue().contains(Host.builder().label("All-Hosts").build())) {
			selectedHosts.clear();
			selectedHosts.select(hosts.stream().filter(e -> !e.getLabel().equals("All-Hosts")).toArray(Host[]::new));
		}

		connector = new MultiHostConnector(selectedHosts.getValue(), cmd -> {
			cmd.command(action);
		}, push -> {
			if (push.isFinished()) {
				System.out.println("finished host: " + push.getHost());
				gridData.remove(HostGridData.builder().connector(push).host(push.getHost()).build());
				gridData.add(HostGridData.builder().connector(push).host(push.getHost()).build());
			}
		}, false, false);

		runWhileAttachedStoppable(this, () -> {
			connector.start();
			if (connector != null && connector.isRunning()) {
				String output = connector.getOutput();
				if (output != null && !output.isEmpty()) {
					console.setAutoScroll(true);
					console.append(output);
					gridHostStatus.getDataProvider().refreshAll();
				} else {
					console.setAutoScroll(false);
				}
			}
		}, 1500, 500, false);

	}

	public MPanel createBottom() {
		MHorizontalLayout layout = new MHorizontalLayout().withFullSize().withMargin(false).withSpacing(false);

		MPanel left = createLeftPanel();

		if (singleHost) {
			left.setVisible(false);
		}

		MPanel right = createRightPanel();

		layout.addComponents(left, right);
		layout.setExpandRatio(left, 0.25f);
		layout.setExpandRatio(right, 0.75f);
		return new MPanel(layout).withFullSize();
	}

	public MPanel createHeader() {
		selectedHosts = new ComboBoxMultiselect<Host>();
		selectedHosts.setItemCaptionGenerator(e -> e.getLabel());
		selectedHosts.setItems(this.hosts);
		selectedHosts.setStyleName(ValoTheme.COMBOBOX_BORDERLESS);

		if (singleHost) {
			selectedHosts.setVisible(false);
		}

		searchfield = new AutocompleteTextField();
		searchfield.setPlaceholder("> ... instant action with ctrl+enter ...");

		searchfield.setDelay(150); // Delay before sending a query to the server
		searchfield.setItemAsHtml(false);// Suggestions contain html formating. If true, make sure that the html is save
		searchfield.setMinChars(1);// The required value length to trigger a query
		searchfield.setScrollBehavior(ScrollBehavior.NONE); // The method that should be used to compensate scrolling of
															// the
		// page
		searchfield.setSuggestionLimit(0);// The max amount of suggestions send to the client. If the limit is >= 0 no
		// limit is applied

		searchfield.setSuggestionProvider(new SearchSuggestionProvider());
		searchfield.addValueChangeListener(e -> {
		});

		Utils.addKeyListener(action -> {
			startAction(searchfield.getValue());
		}, searchfield, KeyCode.ENTER, ModifierKey.CTRL);

		searchfield.setStyleName(ValoTheme.COMBOBOX_BORDERLESS);
		searchfield.setWidth(100, Unit.PERCENTAGE);

		start = new MButton().withIcon(MaterialIcons.PLAY_ARROW).withStyleName(ValoTheme.BUTTON_FRIENDLY);
		start.withListener(e -> {
			if (hosts.isEmpty()) {
				UtilsNotification.error("Please select at least one host");
				return;
			}
			if (searchfield.getValue().isEmpty()) {
				UtilsNotification.error("Please select an action ");
				return;
			}

			new ConfirmDialog("Start: " + searchfield.getValue(), ok -> {
				UtilsNotification.notificationTray("Starting Action", searchfield.getValue());
				startAction(searchfield.getValue());
			}, discard -> {
				UtilsNotification.discard();
			});
		});

		stop = new MButton().withIcon(MaterialIcons.STOP).withStyleName(ValoTheme.BUTTON_DANGER).withListener(e -> {
			new ConfirmDialog("Stop: " + searchfield.getValue(), ok -> {
				try {
					UtilsNotification.notificationTray("Stopping Action", searchfield.getValue());
					if (connector != null) {
						connector.stop();
						consoleThreadUI.interrupt();
					}
				} catch (Exception e2) {
				}

			}, discard -> {
			});
		});

		clear = new MButton().withIcon(MaterialIcons.DELETE_FOREVER).withListener(e -> {
			new ConfirmDialog("Clear: " + searchfield.getValue(), ok -> {
				UtilsNotification.notificationTray("Clear all", "");
				console.clear();
			}, discard -> {
			});
		});

		return new MPanel(new MHorizontalLayout(selectedHosts, searchfield, start, stop, clear).withMargin(false)
				.withSpacing(false).expand(searchfield).withFullWidth()).withFullWidth();
	}

	public void clearAll() {
		try {
			UtilsNotification.notificationTray("Clear data", "");
			searchfield.setValue("");
			selectedHosts.setValue(null);
		} catch (Exception e) {
		}

		try {
			gridData.clear();
		} catch (Exception e) {
		}

		try {
			console.clear();
		} catch (Exception e) {
		}

	}

	public MPanel createLeftPanel() {
		gridHostStatus = new MGrid<HostGridData>(HostGridData.class).withFullSize();
		gridHostStatus.addColumn(e -> e.getHost().getLabel()).setId("label").setCaption("Label");
		gridHostStatus.addColumn(e -> {
			if (!e.getConnector().isErrors()) {
				return FontAwesome.CHECK.getHtml();
			} else {
				return FontAwesome.TIMES.getHtml();
			}
		}, new HtmlRenderer()).setId("status").setCaption("Status").setStyleGenerator(e -> {
			if (!e.getConnector().isErrors()) {
				return STYLES.ICON_GREEN;
			} else {
				return STYLES.ICON_RED;
			}
		});

		gridHostStatus.addItemClickListener(e -> {
			// WindowOpenerButton btn = new WindowOpenerButton(
			// PopupConfiguration.builder().width(850).height(550).build().addParam("theme",
			// ETheme.Dark.getTheme()),
			// new
			// MultiHostConsole(Stream.of(e.getItem().getHost()).collect(Collectors.toSet()),
			// true, true, true,
			// true, ELinuxActions.PING.getCmd()));
			// btn.click();
			MultiHostConsole console = new MultiHostConsole(
					Stream.of(e.getItem().getHost()).collect(Collectors.toSet()), true, true, true, true,
					ELinuxActions.PING.getCmd());
			UtilsWindow.createWindow("Host: " + e.getItem().getHost().getLabel(), console, EWindowSize.Normal, true);
		});

		gridHostStatus.setColumns("label", "status");
		gridHostStatus.setItems(gridData);
		return new MPanel(gridHostStatus).withFullSize();
	}

	public MPanel createRightPanel() {
		console = new HostConsole();
		console.setSizeFull();
		return new MPanel(console).withFullSize();
	}

	@Override
	public void build() {
		this.hosts.add(Host.builder().label("All-Hosts").build());

		if (initActions != null)
			this.actions = Stream.of(initActions).collect(Collectors.toSet());

		actions.addAll(Stream.of(ELinuxActions.values()).map(e -> e.getCmd()).collect(Collectors.toSet()));

		setSizeFull();
		withMargin(false);
		withSpacing(false);

		MPanel top = createHeader();
		MPanel bottom = createBottom();

		addComponents(top, bottom);
		expand(bottom);
	}

	@Override
	public void init() {

	}

	public MGrid<HostGridData> getGridHostStatus() {
		return gridHostStatus;
	}

	public HostConsole getConsole() {
		return console;
	}

	public Thread runWhileAttachedStoppable(final Component component, final Runnable task, final long interval,
			final long initialPause, boolean Synchronized) {

		consoleThreadUI = new Thread() {
			@Override
			public void run() {
				try {
					Thread.sleep(initialPause);

					while (component.getUI() != null && component.getUI().isAttached()) {
						if (Synchronized) {
							component.getUI().accessSynchronously(task);
						} else {
							try {
								Future<Void> future = component.getUI().access(task);
								future.get();
							} catch (Exception e) {
								return;
							}

						}
						if (interval <= 0) {
							break;
						}
						Thread.sleep(interval);

					}
				} catch (Exception e) {
					getLogger().error("Terminated runWhileAttached " + e);
				}
			}

		};
		consoleThreadUI.start();
		return consoleThreadUI;
	}

	@Override
	public void detach() {
		try {
			connector.stop();
		} catch (Exception e) {
		}

		super.detach();
	}

}
