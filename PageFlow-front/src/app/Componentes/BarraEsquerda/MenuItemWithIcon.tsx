// BarraEsquerda/MenuItemWithIcon.tsx
"use client";

import { MenuItem, IconWrapper, IconLabel } from "./styles";

interface MenuItemWithIconProps {
  icon: string;
  label: string;
  selected?: boolean;
  expanded: boolean;
  onClick: () => void;
}

export function MenuItemWithIcon({
  icon,
  label,
  selected,
  expanded,
  onClick,
}: MenuItemWithIconProps) {
  return (
    <MenuItem $selected={selected} onClick={onClick}>
      <IconWrapper><i className={icon} /></IconWrapper>
      {expanded && <IconLabel>{label}</IconLabel>}
    </MenuItem>
  );
}
