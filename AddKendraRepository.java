package com.hsbc.hap.cdr.dto;

import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CERResponseDTO {
    private CpMaster cpMaster;
    private List<EngagementPlugin> engagementPlugins;
    private EngagementTarget engagementTarget;
    private List<WorkspaceTarget> workspaceTargets;
}
