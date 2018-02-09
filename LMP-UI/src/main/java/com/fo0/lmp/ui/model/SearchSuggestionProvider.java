package com.fo0.lmp.ui.model;

import java.util.Collection;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.vaadin.alump.materialicons.MaterialIcons;

import com.fo0.lmp.ui.data.ActionManager;
import com.fo0.lmp.ui.enums.ELinuxActions;
import com.fo0.lmp.ui.utils.STYLES;
import com.vaadin.server.Resource;

import eu.maxschuster.vaadin.autocompletetextfield.AutocompleteQuery;
import eu.maxschuster.vaadin.autocompletetextfield.AutocompleteSuggestion;
import eu.maxschuster.vaadin.autocompletetextfield.provider.CollectionSuggestionProvider;
import eu.maxschuster.vaadin.autocompletetextfield.provider.MatchMode;

public class SearchSuggestionProvider extends CollectionSuggestionProvider {

	private static final long serialVersionUID = 7479751017123686123L;

	private final Resource imageIcon = MaterialIcons.SEARCH;
	private boolean addDescription = true;
	private String addStyleNames = STYLES.BG_COLOR_SEARCHBOX_SUGGESTIONS;

	public SearchSuggestionProvider() {
		super(Stream.concat(basicCommands(), customCommands()).collect(Collectors.toSet()), MatchMode.CONTAINS, true);

	}

	@Override
	public Collection<AutocompleteSuggestion> querySuggestions(AutocompleteQuery query) {
		Collection<AutocompleteSuggestion> suggestions = super.querySuggestions(query);
		Map<String, String> map = ActionManager.createMapWithDescriptions();

		for (AutocompleteSuggestion suggestion : suggestions) {
			String description = map.get(suggestion.getValue());
			if (description != null && !description.isEmpty()) {
				suggestion.setDescription(description);
			}

			suggestion.setIcon(imageIcon);

			if (addStyleNames != null) {
				suggestion.addStyleName(addStyleNames);
			}
		}
		return suggestions;
	}

	public boolean isAddDescription() {
		return addDescription;
	}

	public void setAddDescription(boolean addDescription) {
		this.addDescription = addDescription;
	}

	public String getAddStyleNames() {
		return addStyleNames;
	}

	public void setAddStyleNames(String addStyleNames) {
		this.addStyleNames = addStyleNames;
	}

	private static Stream<String> basicCommands() {
		return Stream.of(ELinuxActions.values()).map(e -> e.getCmd());
	}

	private static Stream<String> customCommands() {
		return ActionManager.load().stream().filter(e -> e.isActive()).map(e -> e.getCommand());
	}

}
