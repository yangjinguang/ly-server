package com.liyu.server.service.impl;

import com.liyu.server.enums.ContactStatusEnum;
import com.liyu.server.enums.ContactTypeEnum;
import com.liyu.server.service.ContactService;
import com.liyu.server.tables.pojos.Contact;
import com.liyu.server.tables.records.ContactRecord;
import com.liyu.server.utils.CommonUtils;
import org.jooq.DSLContext;
import org.jooq.exception.NoDataFoundException;
import org.jooq.types.ULong;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

import java.util.List;

import static com.liyu.server.tables.Contact.CONTACT;
import static com.liyu.server.tables.OrganizationContact.ORGANIZATION_CONTACT;

@Service
public class ContactServiceImpl implements ContactService {
    @Resource
    private DSLContext context;

    @Override
    public Integer countByTenantId(String tenantId) {
        return context.selectCount().from(CONTACT).where(CONTACT.TENANT_ID.eq(tenantId)).fetchOne().into(int.class);
    }

    @Override
    public List<Contact> listByTenantId(String tenantId, Integer offset, Integer size) {
        return context.selectFrom(CONTACT).where(CONTACT.TENANT_ID.eq(tenantId)).fetch().into(Contact.class);
    }

    @Override
    public Contact create(Contact newContact, String accountId, String tenantId) {
        return context.insertInto(CONTACT).columns(
                CONTACT.CONTACT_ID,
                CONTACT.ACCOUNT_ID,
                CONTACT.TENANT_ID,
                CONTACT.NAME,
                CONTACT.AVATAR,
                CONTACT.PHONE,
                CONTACT.EMAIL,
                CONTACT.CONTACT_TYPE
        ).values(
                CommonUtils.UUIDGenerator(),
                accountId,
                tenantId,
                newContact.getName(),
                newContact.getAvatar(),
                newContact.getPhone(),
                newContact.getEmail(),
                newContact.getContactType()
        ).returning().fetchOne().into(Contact.class);
    }

    @Override
    public Contact update(ULong id, Contact newContact) {
        ContactRecord contactRecord = context.selectFrom(CONTACT).where(CONTACT.ID.eq(id))
                .fetchOptional()
                .orElseThrow(() -> new NoDataFoundException("联系人不存在"));
        String name = newContact.getName();
        if (name != null && !name.isEmpty()) {
            contactRecord.setName(name);
        }
        String avatar = newContact.getAvatar();
        if (avatar != null && !avatar.isEmpty()) {
            contactRecord.setAvatar(avatar);
        }
        String phone = newContact.getPhone();
        if (phone != null && !phone.isEmpty()) {
            contactRecord.setPhone(phone);
        }
        String email = newContact.getEmail();
        if (email != null && !email.isEmpty()) {
            contactRecord.setEmail(email);
        }
        ContactTypeEnum contactType = newContact.getContactType();
        if (contactType != null) {
            contactRecord.setContactType(contactType);
        }
        return contactRecord.into(Contact.class);
    }

    @Override
    public List<Contact> getByAccountId(String accountId) {
        return context.selectFrom(CONTACT).where(CONTACT.ACCOUNT_ID.eq(accountId)).fetch().into(Contact.class);
    }

    @Override
    public Contact getByAccountIdAndTenantId(String accountId, String tenantId) {
        return context.selectFrom(CONTACT).where(CONTACT.ACCOUNT_ID.eq(accountId), CONTACT.TENANT_ID.eq(tenantId))
                .fetchOptional()
                .orElseThrow(() -> new NoDataFoundException("未找到用户"))
                .into(Contact.class);
    }

    @Override
    public Contact getById(ULong id) {
        return context.selectFrom(CONTACT)
                .where(CONTACT.ID.eq(id))
                .fetchOptional()
                .orElseThrow(() -> new NoDataFoundException("未找到此联系人"))
                .into(Contact.class);
    }


    @Override
    public void bindOrganization(String contactId, String organizationId) {
        Integer count = context.selectCount().from(ORGANIZATION_CONTACT).where(ORGANIZATION_CONTACT.CONTACT_ID.eq(contactId), ORGANIZATION_CONTACT.ORGANIZATION_ID.eq(organizationId)).fetchOne().into(int.class);
        if (count <= 0) {
            context.insertInto(ORGANIZATION_CONTACT).columns(
                    ORGANIZATION_CONTACT.CONTACT_ID,
                    ORGANIZATION_CONTACT.ORGANIZATION_ID
            ).values(
                    contactId,
                    organizationId
            ).execute();
        }
    }

    @Override
    public void bindOrganizations(String contactId, List<String> organizationIds) {
        for (String organizationId : organizationIds) {
            this.bindOrganization(contactId, organizationId);
        }
    }

    @Override
    public void unbindOrganizationsAll(String contactId) {
        context.deleteFrom(ORGANIZATION_CONTACT).where(ORGANIZATION_CONTACT.CONTACT_ID.eq(contactId)).execute();
    }

    @Override
    public Contact changeStatus(ULong id, ContactStatusEnum status) {
        ContactRecord contactRecord = context.selectFrom(CONTACT)
                .where(CONTACT.ID.eq(id))
                .fetchOptional()
                .orElseThrow(() -> new NoDataFoundException("contact not found"));
        contactRecord.setStatus(status);
        contactRecord.update();
        return contactRecord.into(Contact.class);
    }


}
