package com.iteminfocopier;

import com.google.inject.Provides;
import javax.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.*;
import net.runelite.api.Menu;
import net.runelite.api.events.MenuOpened;
import net.runelite.api.gameval.InterfaceID;
import net.runelite.api.widgets.Widget;
import net.runelite.api.widgets.WidgetUtil;
import net.runelite.client.chat.ChatMessageManager;
import net.runelite.client.chat.QueuedMessage;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.game.ItemManager;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;

import java.awt.*;
import java.awt.datatransfer.StringSelection;

@Slf4j
@PluginDescriptor(
		name = "Item Info Copier"
)
public class ItemInfoCopierPlugin extends Plugin
{
	@Inject
	private Client client;

	@Inject
	private ItemInfoCopierConfig config;

	@Inject
	private ChatMessageManager chatMessageManager;

	@Inject
	private ItemManager itemManager;

	@Override
	protected void startUp() throws Exception
	{
		log.debug("Item Info Copier started!");
	}

	@Override
	protected void shutDown() throws Exception
	{
		log.debug("Item Info Copier stopped!");
	}

	@Provides
	ItemInfoCopierConfig provideConfig(ConfigManager configManager)
	{
		return configManager.getConfig(ItemInfoCopierConfig.class);
	}

	@Subscribe
	public void onMenuOpened(final MenuOpened event)
	{
		if (!config.enableCopyId() && !config.enableCopyName())
		{
			return;
		}

		if (config.requireShift() && !client.isKeyPressed(KeyCode.KC_SHIFT))
		{
			return;
		}

		final MenuEntry[] entries = event.getMenuEntries();

		for (int idx = entries.length - 1; idx >= 0; --idx)
		{
			final MenuEntry entry = entries[idx];
			final Widget w = entry.getWidget();

			if (w != null && WidgetUtil.componentToInterface(w.getId()) == InterfaceID.INVENTORY
					&& "Examine".equals(entry.getOption()) && entry.getIdentifier() == 10)
			{
				final int itemId = w.getItemId();
				final String itemName = itemManager.getItemComposition(itemId).getMembersName();

				final MenuEntry parent = client.createMenuEntry(idx)
						.setOption("Copy")
						.setTarget(entry.getTarget())
						.setType(MenuAction.RUNELITE);

				final Menu submenu = parent.createSubMenu();

				if (config.enableCopyId())
				{
					submenu.createMenuEntry(0)
							.setOption("Item ID")
							.setType(MenuAction.RUNELITE)
							.onClick(e ->
							{
								Toolkit.getDefaultToolkit()
										.getSystemClipboard()
										.setContents(new StringSelection(String.valueOf(itemId)), null);

								chatMessageManager.queue(QueuedMessage.builder()
										.type(ChatMessageType.CONSOLE)
										.runeLiteFormattedMessage("Item ID copied to clipboard")
										.build());
							});
				}

				if (config.enableCopyName())
				{
					submenu.createMenuEntry(0)
							.setOption("Item name")
							.setType(MenuAction.RUNELITE)
							.onClick(e ->
							{
								Toolkit.getDefaultToolkit()
										.getSystemClipboard()
										.setContents(new StringSelection(itemName), null);

								chatMessageManager.queue(QueuedMessage.builder()
										.type(ChatMessageType.CONSOLE)
										.runeLiteFormattedMessage("Item name copied to clipboard")
										.build());
							});
				}
			}
		}
	}
}
