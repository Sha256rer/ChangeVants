package com.Behaviours;

import eu.darkbot.api.PluginAPI;
import eu.darkbot.api.config.ConfigSetting;
import eu.darkbot.api.config.annotations.*;
import eu.darkbot.api.config.annotations.Number;
import eu.darkbot.api.extensions.*;
import eu.darkbot.api.game.entities.Entity;
import eu.darkbot.api.game.entities.Npc;
import eu.darkbot.api.game.items.SelectableItem;
import eu.darkbot.api.managers.BotAPI;
import eu.darkbot.api.managers.EntitiesAPI;
import eu.darkbot.api.managers.HeroAPI;
import eu.darkbot.api.managers.HeroItemsAPI;

import java.util.*;
import java.util.stream.Collectors;

@Feature( name= "ChangeVants", description = "Changes Vants on Conditions")
public class ChangeVants implements Behavior, InstructionProvider, Configurable<ChangeVants.Config> {
    @Configuration("change_vants.config")
    public static class Config {

        @Percentage

        public double PERCENTAGE_VALUE = 0.0;

        @Dropdown (options = Formations.class)

        public SelectableItem.Formation FORMATION1 = Formations.OPTIONS.get(0);

        @Dropdown (options = Formations.class)

        public SelectableItem.Formation FORMATION2 = Formations.OPTIONS.get(0);


    }
    public static class Formations implements Dropdown.Options<SelectableItem.Formation>{
        private static final List<SelectableItem.Formation> OPTIONS = Arrays.stream(SelectableItem.Formation.values()).collect(Collectors.toList());

        @Override
        public Collection<SelectableItem.Formation> options() {
            return OPTIONS;
        }


    }
    private Config config;
    private final PluginAPI api;
    private final BotAPI bot;
    private final HeroAPI hero;
    private final HeroItemsAPI items;
    private  Npc target;
    public ChangeVants(EntitiesAPI E, HeroAPI H, HeroItemsAPI I, PluginAPI P, BotAPI B){
    this.hero = H;
    this.api = P;
    this.bot = B;
    this.items = I;
    }
    @Override
    public void onTickBehavior() {
        target= hero.getTargetAs(Npc.class);

    if (target != null){
      if((target.getHealth().getShield()>config.PERCENTAGE_VALUE*target.getHealth().getMaxShield())){
          items.useItem(config.FORMATION1);

      }
      else{
          items.useItem(config.FORMATION2);
      }
        }
    }
    public String instructions() {
        return "Set the formations you need";
    }


    @Override
    public void onStoppedBehavior() {
        Behavior.super.onStoppedBehavior();
    }

    @Override
    public void setConfig(ConfigSetting<Config> configSetting) {
        this.config = configSetting.getValue();
    }
}
