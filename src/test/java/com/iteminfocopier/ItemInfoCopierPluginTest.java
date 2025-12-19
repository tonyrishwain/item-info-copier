package com.iteminfocopier;

import net.runelite.client.RuneLite;
import net.runelite.client.externalplugins.ExternalPluginManager;

public class ItemInfoCopierPluginTest
{
	public static void main(String[] args) throws Exception
	{
		ExternalPluginManager.loadBuiltin(ItemInfoCopierPlugin.class);
		RuneLite.main(args);
	}
}