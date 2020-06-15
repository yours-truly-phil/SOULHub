INSERT INTO soulpatches(id, name, description,
                        created_at, updated_at,
                        soul_file_name, soul_file_content,
                        soulpatch_file_name, soulpatch_file_content,
                        offset_date_time, author, no_servings)
    VALUES('soulpatch1 name',
           'soulpatch1 description',
           current_timestamp,
           current_timestamp,
           'soulpatch1 soulFileName',
           'soulpatch1 soulFileContent',
           'soulpatch1 soulpatchFileName',
           'soulpatch1 soulpatchFileContent',
           current_timestamp,
           'soulpatch1 author',
           4711);

INSERT INTO soulpatches(id, name, description,
                        created_at, updated_at,
                        soul_file_name, soul_file_content,
                        soulpatch_file_name, soulpatch_file_content,
                        offset_date_time, author, no_servings)
VALUES(
       'soulpatch2 name',
       'soulpatch2 description',
       current_timestamp,
       current_timestamp,
       'soulpatch2 soulFileName',
       'soulpatch2 soulFileContent',
       'soulpatch2 soulpatchFileName',
       'soulpatch2 soulpatchFileContent',
       current_timestamp,
       'soulpatch2 author',
       815);