/*! ******************************************************************************
 *
 * Pentaho
 *
 * Copyright (C) 2024 by Hitachi Vantara, LLC : http://www.pentaho.com
 *
 * Use of this software is governed by the Business Source License included
 * in the LICENSE.TXT file.
 *
 * Change Date: 2029-07-20
 ******************************************************************************/


package org.pentaho.reporting.libraries.repository.zipreader;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.pentaho.reporting.libraries.base.util.IOUtils;
import org.pentaho.reporting.libraries.repository.ContentCreationException;
import org.pentaho.reporting.libraries.repository.ContentEntity;
import org.pentaho.reporting.libraries.repository.ContentIOException;
import org.pentaho.reporting.libraries.repository.ContentItem;
import org.pentaho.reporting.libraries.repository.ContentLocation;
import org.pentaho.reporting.libraries.repository.LibRepositoryBoot;
import org.pentaho.reporting.libraries.repository.Repository;
import org.pentaho.reporting.libraries.repository.RepositoryUtilities;

import java.util.Date;
import java.util.HashMap;
import java.util.zip.ZipEntry;

/**
 *
 *
 */
public class ZipReadContentLocation implements ContentLocation {
  private static final Log logger = LogFactory.getLog( ZipReadContentLocation.class );
  private ZipReadRepository repository;
  private ZipReadContentLocation parent;
  private String comment;
  private String name;
  private long size;
  private long time;
  private String entryName;
  private HashMap entries;

  public ZipReadContentLocation( final ZipReadRepository repository,
                                 final ZipReadContentLocation parent,
                                 final String entryName ) {
    if ( repository == null ) {
      throw new NullPointerException();
    }
    if ( entryName == null ) {
      throw new NullPointerException();
    }

    this.repository = repository;
    this.parent = parent;
    this.entryName = entryName;
    this.entries = new HashMap();
    this.name = RepositoryUtilities.buildName( this, "/" ) + '/';
  }

  public ZipReadContentLocation( ZipReadRepository repository, ZipReadContentLocation parent, ZipEntry zipEntry ) {
    if ( repository == null ) {
      throw new NullPointerException();
    }
    if ( parent == null ) {
      throw new NullPointerException();
    }
    if ( zipEntry == null ) {
      throw new NullPointerException();
    }

    this.repository = repository;
    this.parent = parent;
    this.comment = zipEntry.getComment();
    this.size = zipEntry.getSize();
    this.time = zipEntry.getTime();
    this.entries = new HashMap();
    this.entryName = IOUtils.getInstance().getFileName( zipEntry.getName() );
    this.name = RepositoryUtilities.buildName( this, "/" ) + '/';
  }

  private void updateMetaData( final ZipEntry zipEntry ) {
    this.comment = zipEntry.getComment();
    this.size = zipEntry.getSize();
    this.time = zipEntry.getTime();
  }


  public void updateDirectoryEntry( final String[] name, final int index, final ZipEntry zipEntry ) {
    if ( name == null ) {
      throw new NullPointerException();
    }
    if ( zipEntry == null ) {
      throw new NullPointerException();
    }

    final String path = name[ index ];
    final Object entry = entries.get( path );
    if ( entry instanceof ContentItem ) {
      logger.warn( "Directory-Entry with the same name as a Content-Entry encountered: " + path );
      return;
    }
    final ZipReadContentLocation location;
    if ( entry == null ) {
      location = new ZipReadContentLocation( repository, this, path );
      entries.put( path, location );
    } else {
      location = (ZipReadContentLocation) entry;
    }
    final int nextNameIdx = index + 1;
    if ( nextNameIdx < name.length ) {
      location.updateDirectoryEntry( name, nextNameIdx, zipEntry );
    } else if ( nextNameIdx == name.length ) {
      location.updateMetaData( zipEntry );
    }
  }

  public void updateEntry( final String[] name, final int index, final ZipEntry zipEntry, final byte[] data ) {
    if ( name == null ) {
      throw new NullPointerException();
    }
    if ( zipEntry == null ) {
      throw new NullPointerException();
    }
    if ( data == null ) {
      throw new NullPointerException();
    }

    final String path = name[ index ];
    final Object entry = entries.get( path );
    final int nextNameIdx = index + 1;

    if ( nextNameIdx < name.length ) {
      if ( entry instanceof ContentItem ) {
        logger.warn( "Directory-Entry with the same name as a Content-Entry encountered: " + path );
        return;
      }


      final ZipReadContentLocation location;
      if ( entry == null ) {
        location = new ZipReadContentLocation( repository, this, path );
        entries.put( path, location );
      } else {
        location = (ZipReadContentLocation) entry;
      }
      if ( nextNameIdx < name.length ) {
        location.updateEntry( name, nextNameIdx, zipEntry, data );
      }
    } else if ( nextNameIdx == name.length ) {
      if ( entry instanceof ContentItem ) {
        logger.warn( "Duplicate Content-Entry encountered: " + path );
        return;
      } else if ( entry != null ) {
        logger.warn( "Replacing Directory-Entry with the same name as a Content-Entry: " + path );
      }
      final ZipReadContentItem contentItem = new ZipReadContentItem( repository, this, zipEntry, data );
      entries.put( path, contentItem );
    }
  }

  public ContentEntity[] listContents() throws ContentIOException {
    return (ContentEntity[]) entries.values().toArray( new ContentEntity[ entries.size() ] );
  }

  public ContentEntity getEntry( final String name ) throws ContentIOException {
    final ContentEntity contentEntity = (ContentEntity) entries.get( name );
    if ( contentEntity == null ) {
      throw new ContentIOException( "Not found:" + name );
    }
    return contentEntity;
  }

  public boolean exists( final String name ) {
    return entries.containsKey( name );
  }

  public ContentItem createItem( final String name ) throws ContentCreationException {
    throw new ContentCreationException();
  }

  public ContentLocation createLocation( final String name ) throws ContentCreationException {
    throw new ContentCreationException();
  }

  public String getName() {
    return entryName;
  }

  public Object getContentId() {
    return name;
  }

  public Object getAttribute( final String domain, final String key ) {
    if ( LibRepositoryBoot.REPOSITORY_DOMAIN.equals( domain ) ) {
      if ( LibRepositoryBoot.SIZE_ATTRIBUTE.equals( key ) ) {
        return new Long( size );
      } else if ( LibRepositoryBoot.VERSION_ATTRIBUTE.equals( key ) ) {
        return new Date( time );
      }
    } else if ( LibRepositoryBoot.ZIP_DOMAIN.equals( domain ) ) {
      if ( LibRepositoryBoot.ZIP_COMMENT_ATTRIBUTE.equals( key ) ) {
        return comment;
      }
    }
    return null;
  }

  public boolean setAttribute( final String domain, final String key, final Object value ) {
    return false;
  }

  public ContentLocation getParent() {
    return parent;
  }

  public Repository getRepository() {
    return repository;
  }

  public boolean delete() {
    return false;
  }
}
