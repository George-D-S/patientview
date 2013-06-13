package com.solidstategroup.radar.service.hnf1b;

import com.solidstategroup.radar.model.hnf1b.HNF1BMisc;

public interface HNF1BMiscManager {

    void save(HNF1BMisc hnf1BMisc);

    HNF1BMisc get(Long radarNo);

}
