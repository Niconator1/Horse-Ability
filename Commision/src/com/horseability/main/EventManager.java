package com.horseability.main;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Horse;
import org.bukkit.entity.Horse.Color;
import org.bukkit.entity.Horse.Style;
import org.bukkit.entity.Horse.Variant;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.util.Vector;
import org.spigotmc.event.entity.EntityMountEvent;

public class EventManager implements Listener {
	@EventHandler
	public void doRightClick(PlayerInteractEvent e) {
		Player p = e.getPlayer();
		if (e.getItem() != null) {
			if (e.getItem().getType() == Material.LEASH) {
				if (!(p.getExp() > 0)) {
					double yaw = ((p.getLocation().getYaw() + 90) * Math.PI) / 180;
					double x = Math.cos(yaw);
					double z = Math.sin(yaw);
					Horse[] horses = new Horse[5];
					Vector direction = new Vector(x, 0, z).normalize();
					Vector vec = HorseUtil.rotateAroundAxisY(direction.clone().multiply((horses.length - 1) * 0.8),
							0.5 * Math.PI);
					Location start = p.getLocation().add(direction.clone().multiply(2.5)).add(vec);
					for (int i = 0; i < horses.length; i++) {
						Horse h = (Horse) p.getWorld().spawnEntity(start, EntityType.HORSE);
						start.add(vec.clone().multiply(-2.0 * 1.0 / (horses.length - 1.0)));
						h.setAdult();
						h.setVariant(Variant.HORSE);
						h.setStyle(Style.WHITE);
						h.setVelocity(direction.clone().multiply(7.7 * 0.05).setY(h.getVelocity().getY()));
						h.setAI(false);
						h.setColor(Color.WHITE);
						h.setInvulnerable(true);
						horses[i] = h;
						HorseAbility ha = new HorseAbility(p, horses, direction);
						HorsePlugin.ha.add(ha);
					}
					p.setLevel(60);
					p.setExp(0.99f);
				} else {
					p.sendMessage(
							ChatColor.RED + "You need to wait " + p.getLevel() + " seconds before using this again.");
				}
				e.setCancelled(true);
			}
		}
	}

	@EventHandler
	public void doMount(EntityMountEvent e) {
		for (int i = 0; i < HorsePlugin.ha.size(); i++) {
			HorseAbility ha = HorsePlugin.ha.get(i);
			for (int j = 0; j < ha.getHorses().length; j++) {
				Horse h = ha.getHorses()[j];
				if (h.getUniqueId().compareTo(e.getMount().getUniqueId()) == 0) {
					e.setCancelled(true);
				}
			}
		}
	}

}
