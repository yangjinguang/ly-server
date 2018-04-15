package com.liyu.server.service.impl;

import com.liyu.server.service.OrganizationService;
import com.liyu.server.tables.pojos.Organization;
import com.liyu.server.tables.records.OrganizationRecord;
import lombok.extern.slf4j.Slf4j;
import org.jooq.DSLContext;
import org.jooq.types.ULong;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import static com.liyu.server.tables.Organization.ORGANIZATION;

@Slf4j
@Service
public class OrganizationServiceImpl implements OrganizationService {
    @Resource
    private DSLContext context;

    @Override
    public List<Organization> list() {
        return context.selectFrom(ORGANIZATION).fetch().into(Organization.class);
    }

    @Override
    public Organization create(Organization newOrganization) {
        Timestamp currentTime = new Timestamp(new Date().getTime());
        return context.insertInto(ORGANIZATION).columns(
                ORGANIZATION.NAME,
                ORGANIZATION.ADDRESS,
                ORGANIZATION.IS_CLASS,
                ORGANIZATION.PARENT_ID,
                ORGANIZATION.TENANT_ID,
                ORGANIZATION.CREATED_AT,
                ORGANIZATION.UPDATED_AT
        ).values(
                newOrganization.getName(),
                newOrganization.getAddress(),
                newOrganization.getIsClass(),
                newOrganization.getParentId(),
                newOrganization.getTenantId(),
                currentTime,
                currentTime
        ).returning().fetchOne().into(Organization.class);
    }

    @Override
    public Organization update(ULong id, Organization newOrganization) {
        OrganizationRecord organizationRecord = context.selectFrom(ORGANIZATION).where(ORGANIZATION.ID.eq(id)).fetchOne();
        if (!newOrganization.getName().isEmpty()) {
            organizationRecord.setName(newOrganization.getName());
        }
        if (!newOrganization.getAddress().isEmpty()) {
            organizationRecord.setAddress(newOrganization.getAddress());
        }
        if (newOrganization.getIsClass() != null) {
            organizationRecord.setIsClass(newOrganization.getIsClass());
        }
        if (newOrganization.getParentId() != null) {
            organizationRecord.setParentId(newOrganization.getParentId());
        }
        if (newOrganization.getTenantId() != null) {
            organizationRecord.setTenantId(newOrganization.getTenantId());
        }
        organizationRecord.update();
        return organizationRecord.into(Organization.class);
    }

    @Override
    public void delete(ULong id) {
        context.deleteFrom(ORGANIZATION).where(ORGANIZATION.ID.eq(id)).execute();
    }
}
