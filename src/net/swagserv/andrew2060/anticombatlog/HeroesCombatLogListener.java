package net.swagserv.andrew2060.anticombatlog;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import com.herocraftonline.heroes.Heroes;
import com.herocraftonline.heroes.characters.Hero;
import com.herocraftonline.heroes.characters.effects.CombatEffect.LeaveCombatReason;
public class HeroesCombatLogListener implements Listener{
	public static boolean ess;
	AntiCombatLog plugin;
	private Heroes heroes = (Heroes)Bukkit.getServer().getPluginManager().getPlugin("Heroes");
	@EventHandler(priority=EventPriority.HIGHEST)
	public void onPlayerKick(PlayerKickEvent event) {
		Hero h = heroes.getCharacterManager().getHero(event.getPlayer());
		if(h.isInCombat() == true) {
			h.leaveCombat(LeaveCombatReason.LOGOUT);
			return;
		}
	}

	@EventHandler(priority=EventPriority.NORMAL)
	public void onPlayerQuit(PlayerQuitEvent event) {
		Player p = event.getPlayer();
		Hero h = heroes.getCharacterManager().getHero(event.getPlayer());
		LivingEntity targetentity = h.getCombatEffect().getLastCombatant();
		boolean combatcheck = h.isInCombat();
		
		if (combatcheck == false) {
			return;
		}
		else {
			if (targetentity instanceof Player){
				String target = ((Player) targetentity).getName();
				p.damage(1000);
				p.setHealth(0);
				h.setHealth(0);
				h.syncHealth();
				h.syncExperience();		
				Bukkit.getServer().broadcastMessage(ChatColor.AQUA + "[" + ChatColor.RED + "NOTICE" + ChatColor.AQUA + "]: " + p.getName() + " just CombatLogged against " + target + " and dropped their items!");
				if(ess = true) {
					Bukkit.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(), "mail send " + p.getName() + " you were killed for combat-logging!");
					return;
				}
				return;
			}
			else {
				h.leaveCombat(LeaveCombatReason.LOGOUT);
				return;
			}
		}
	}
}
