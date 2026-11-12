const root = "rexafkzone";

export const rexafkzone = {
  label: "RexAFKZone",
  items: [
    { label: "Overview", slug: root },
    {
      label: "Getting started",
      items: [
        { label: "Installation", slug: `${root}/getting-started/installation` },
        { label: "Requirements", slug: `${root}/getting-started/requirements` },
      ],
    },
    {
      label: "Configuration",
      items: [
        { label: "Settings", slug: `${root}/configuration/overview` },
        { label: "Messages", slug: `${root}/configuration/messages` },
        { label: "Rewards", slug: `${root}/configuration/rewards` },
      ],
    },
    {
      label: "Reference",
      items: [
        { label: "Commands", slug: `${root}/reference/commands` },
        { label: "Permissions", slug: `${root}/reference/permissions` },
      ],
    },
    {
      label: "Guides",
      items: [
        { label: "Creating a zone", slug: `${root}/guides/creating-a-zone` },
      ],
    },
    { label: "FAQ", slug: `${root}/faq` },
  ],
};
