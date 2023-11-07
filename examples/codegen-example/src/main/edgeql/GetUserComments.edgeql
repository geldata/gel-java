WITH
    module codegen
SELECT Comment {
    author: {
        name, 
        joined_at
    },
    post: {
        title,
        content,
        author: {
            name,
            joined_at
        },
        created_at
    },
    content,
    created_at
}
FILTER .author.id = <uuid>$author_id