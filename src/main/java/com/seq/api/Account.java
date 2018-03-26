package com.seq.api;

import com.seq.exception.*;
import com.seq.http.*;
import com.google.gson.annotations.SerializedName;

import java.util.*;

/**
 * A container for tokens on a ledger.
 */
public class Account {
  /**
   * Unique identifier of the account.
   */
  public String id;

  /**
   * The set of key IDs used for signing transactions that spend from the
   * account.
   */
  @SerializedName("key_ids")
  public List<String> keyIds;

  /**
   * The number of keys required to sign transactions that spend from the account.
   */
  public int quorum;

  /**
   * User-specified key-value data describing the account.
   */
  public Map<String, Object> tags;

  /**
   * A single page of accounts returned from a query.
   */
  public static class Page extends BasePage<Account> {}

  /**
   * Iterable interface for consuming individual accounts from a query.
   */
  public static class ItemIterable extends BaseItemIterable<Account> {
    public ItemIterable(Client client, String path, Query nextQuery) {
      super(client, path, nextQuery, Page.class);
    }
  }

  /**
   * A builder class for listing accounts in the ledger.
   */
  public static class ListBuilder extends BaseQueryBuilder<ListBuilder> {
    /**
     * Executes the query, returning a page of accounts that match the query.
     * @param client ledger API connection object
     * @return a page of accounts
     * @throws ChainException
     */
    public Page getPage(Client client) throws ChainException {
      return client.request("list-accounts", this.next, Page.class);
    }

    /**
     * Executes the query, returning a page of accounts that match the query
     * beginning with provided cursor.
     * @param client ledger API connection object
     * @param cursor string representing encoded query object
     * @return a page of accounts
     * @throws ChainException
     */
    public Page getPage(Client client, String cursor) throws ChainException {
      Query next = new Query();
      next.cursor = cursor;
      return client.request("list-accounts", next, Page.class);
    }

    /**
     * Executes the query, returning an iterable over accounts that match the query.
     * @param client ledger API connection object
     * @return an iterable over accounts
     * @throws ChainException
     */
    public ItemIterable getIterable(Client client) throws ChainException {
      return new ItemIterable(client, "list-accounts", this.next);
    }
  }

  /**
   * A builder for creating accounts in the ledger.
   */
  public static class Builder {
    private String id;
    private Integer quorum;

    @SerializedName("key_ids")
    private List<String> keyIds;

    private Map<String, Object> tags;

    public Builder() {
      this.keyIds = new ArrayList<>();
    }

    /**
     * Creates a new account in the ledger.
     * @param client ledger API connection object
     * @return an account
     * @throws ChainException
     */
    public Account create(Client client) throws ChainException {
      return client.request("create-account", this, Account.class);
    }

    /**
     * Specifies the id for the new account.
     * @param id unique identifier. Will be auto-generated if not provided.
     * @return updated builder
     */
    public Builder setId(String id) {
      this.id = id;
      return this;
    }

    /**
     * Specifies the number of keys required to sign transactions that spend
     * from the account. Defaults to the number of keys provided.
     * @param quorum a number less than or equal to the number of keys
     * @return updated builder
     */
    public Builder setQuorum(int quorum) {
      this.quorum = new Integer(quorum);
      return this;
    }

    /**
     * Adds a key that can be used to sign transactions that spend from the
     * account.
     * @param id the key's ID
     * @return updated builder
     */
    public Builder addKeyId(String id) {
      this.keyIds.add(id);
      return this;
    }

    /**
     * Adds a key-value pair to the account's tags.
     * @param key key of the tag
     * @param value value of the tag
     * @return updated builder
     */
    public Builder addTag(String key, Object value) {
      if (this.tags == null) {
        this.tags = new HashMap<>();
      }
      this.tags.put(key, value);
      return this;
    }

    /**
     * Specifies key-value data that describes the account.
     * @param tags map of tag keys to tag values
     * @return updated builder
     */
    public Builder setTags(Map<String, Object> tags) {
      this.tags = tags;
      return this;
    }
  }

  /**
   * A builder for updating an account's tags.
   */
  public static class TagUpdateBuilder {
    private String id;
    private Map<String, Object> tags;

    /**
     * Specifies the account to be updated.
     * @param id the account's ID
     * @return updated builder
     */
    public TagUpdateBuilder forId(String id) {
      this.id = id;
      return this;
    }

    /**
     * Specifies a new set of tags.
     * @param tags map of tag keys to tag values
     * @return updated builder
     */
    public TagUpdateBuilder setTags(Map<String, Object> tags) {
      this.tags = tags;
      return this;
    }

    /**
     * Updates the account's tags.
     * @param client ledger API connection object
     * @throws ChainException
     */
    public void update(Client client) throws ChainException {
      client.request("update-account-tags", this, SuccessMessage.class);
    }
  }
}
