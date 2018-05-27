package com.liyu.server.service;

import com.liyu.server.enums.ContactStatusEnum;
import com.liyu.server.tables.pojos.Contact;
import org.jooq.types.ULong;

import java.util.List;

public interface ContactService {
    Integer countByTenantId(String tenantId);

    List<Contact> listByTenantId(String tenantId, Integer offset, Integer size);

    Contact create(Contact newContact, String accountId, String tenantId);

    Contact update(ULong id, Contact newContact);

    List<Contact> getByAccountId(String accountId);

    Contact getByAccountIdAndTenantId(String accountId, String tenantId);

    Contact getById(ULong id);

    void bindOrganization(String contactId, String organizationId);

    void bindOrganizations(String contactId, List<String> organizationIds);

    void unbindOrganizationsAll(String contactId);

    Contact changeStatus(ULong id, ContactStatusEnum status);
}
